package werewolf.controller

import com.example.observer.Observer
import werewolf.model.GameStateModel
import werewolf.model.entities.EndOfRoundAbility
import werewolf.model.entities.EventSignal
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.view.GameActivity
import werewolf.view.GameUiEvent
import werewolf.view.fragments.WinnerTeam
import java.util.PriorityQueue

interface GameController{
    fun setGameView(gameActivity: GameActivity)
}

class GameControllerImpl(
    private val gameStateModel: GameStateModel,
    private val players: MutableList<String>
): GameController {
    private lateinit var gameActivity: GameActivity

    private val priorityQueue: PriorityQueue<EndOfRoundAbility> = PriorityQueue<EndOfRoundAbility>(compareBy { it.getPriority() })
    private var counter = 0
    private var eventsSummary: String = ""
    private var gameLogs: String = ""
    private var gameEnded: Boolean = false

    override fun setGameView(gameActivity: GameActivity) {
        this.gameActivity = gameActivity
        gameActivity.uiEventObservable.subscribe(observer)
        initGame()
    }

    private val observer: Observer<GameUiEvent> =
        Observer { value ->
            when (value) {
                GameUiEvent.ConfirmAction -> confirmAction()
                GameUiEvent.StartTurn -> startTurn()
                GameUiEvent.StartNextRound -> startNextRound()
            }
        }

    private val playerObserver: Observer<EventSignal> =
        Observer { value ->
            when (value.getPlayer().fetchEvent()){
                PlayerEventEnum.SetWerewolfTargets -> setWerewolfTargets(value.getPlayer())
                PlayerEventEnum.SetAlivePlayersTarget -> setAllPlayersTarget(value.getPlayer())
                PlayerEventEnum.SetNoTargets -> setNoTargets(value.getPlayer())
                PlayerEventEnum.SetDeadTargets -> setDeadTargets(value.getPlayer())
                PlayerEventEnum.KilledPlayer -> killedPlayer(value.getPlayer())
                PlayerEventEnum.WerewolfKilled -> werewolfKilled(value.getPlayer())
                PlayerEventEnum.RevivedPlayer -> revivePlayer(value.getPlayer())
                PlayerEventEnum.JesterWin -> jesterWin(value.getPlayer())
            }
        }

    private fun initGame(){
        gameStateModel.initGame(players)
        gameStateModel.getAlivePlayers().forEach {
            player -> player.playerObservable.subscribe(playerObserver)
        }
        setCurrentPlayer()
    }

    private fun setCurrentPlayer(){
        gameActivity.setCurrentPlayer(gameStateModel.getAlivePlayers()[counter].fetchPlayerName())
    }

    private fun confirmAction(){
        counter++
        if(counter<gameStateModel.getAlivePlayers().size){
            setCurrentPlayer()
        }
        else{
            counter = 0
            finishRound()
        }
    }

    private fun startTurn(){
        val player = gameStateModel.getAlivePlayers()[counter]
        player.turnSetUp()
        gameActivity.startTurn(player)
    }

    private fun setWerewolfTargets(player: Player){
        player.defineTargetPlayers(gameStateModel.getAliveVillagers())
    }

    private fun setAllPlayersTarget(player: Player){
        player.defineTargetPlayers(gameStateModel.getAliveVillagers()+gameStateModel.getAliveWerewolves())
    }

    private fun setNoTargets(player: Player){
        player.defineTargetPlayers(listOf())
    }

    private fun setDeadTargets(player: Player){
        player.defineTargetPlayers(gameStateModel.getDeadPlayers())
    }

    private fun killedPlayer(player: Player){
        eventsSummary += player.fetchPlayerName()+" was "+player.fetchDeathCause()+"\n"
        gameLogs += eventsSummary
        val result = gameStateModel.getAliveWerewolves().find { it == player }
        if (result != null) {
            gameStateModel.killWerewolf(player)
        } else {
            gameStateModel.killVillager(player)
        }
    }

    private fun werewolfKilled(player: Player){
        killedPlayer(player)
        ascendWerewolf()
    }

    private fun ascendWerewolf(){
        if(gameStateModel.getAliveWerewolves().isNotEmpty()){
            val werewolf = gameStateModel.ascendWerewolf()
            werewolf.playerObservable.subscribe(playerObserver)
        }
    }

    private fun jesterWin(player: Player){
        gameEnded = true
        gameActivity.gameFinished(listOf(player),WinnerTeam.JESTER, gameLogs)
    }

    private fun revivePlayer(player: Player){
        eventsSummary += player.fetchPlayerName()+" was revived\n"
        gameLogs += eventsSummary
        gameStateModel.revivePlayer(player)
    }

    private fun finishRound(){
        queueAbilities()
        resolveAbilities()
        resetDefenseAndAbilityState()
        checkIfGameEnded()
        if(!gameEnded){
            gameActivity.finishRound(eventsSummary, gameStateModel.getAlivePlayers())
        }
    }

    private fun queueAbilities(){
        gameStateModel.getAlivePlayers().forEach {
            priorityQueue.add(it.fetchEndOfRoundAbility())
            gameLogs+=it.fetchPlayerName()+" ("+it.fetchRole()+") used "+it.fetchUsedAbility()+" on "+ (it.fetchTargetPlayer()?.fetchPlayerName() ?: "no one") +"\n"
        }
    }

    private fun resolveAbilities(){
        while (priorityQueue.isNotEmpty()) {
            val endOfRoundAbility = priorityQueue.poll()
            endOfRoundAbility?.resolve()
        }
    }

    private fun resetDefenseAndAbilityState(){
        gameStateModel.getAlivePlayers().forEach {
            it.resetAbilityState()
            it.resetDefenseState()
        }
    }

    private fun checkIfGameEnded(){
        when {
            gameStateModel.getAliveVillagers().isEmpty() && gameStateModel.getAliveWerewolves().isEmpty() -> {
                gameActivity.gameFinished(emptyList(), WinnerTeam.DRAW, gameLogs)
                gameEnded = true
            }
            gameStateModel.getAliveVillagers().isEmpty() -> {
                gameActivity.gameFinished(gameStateModel.getAliveWerewolves() + gameStateModel.getDeadWerewolves(), WinnerTeam.WEREWOLVES, gameLogs)
                gameEnded = true
            }
            gameStateModel.getAliveWerewolves().isEmpty() -> {
                gameActivity.gameFinished(gameStateModel.getAliveVillagers() + gameStateModel.getDeadVillagers(), WinnerTeam.VILLAGERS, gameLogs)
                gameEnded = true
            }
        }
    }

    private fun startNextRound(){
        eventsSummary = ""
        gameLogs += "------------------------\n"
        checkIfGameEnded()
        if(!gameEnded){
            setCurrentPlayer()
        }
    }
}