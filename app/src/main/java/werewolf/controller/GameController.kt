package werewolf.controller

import com.example.observer.Observer
import werewolf.model.GameSettingsImpl
import werewolf.model.GameStateModel
import werewolf.model.entities.Ability
import werewolf.model.entities.AbilityEventEnum
import werewolf.model.entities.AbilitySignal
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.PlayerSignal
import werewolf.view.GameActivity
import werewolf.view.GameUiEvent
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.RoleNameProvider
import werewolf.view.TargetPlayersEnum
import werewolf.view.TargetPlayersSignal
import werewolf.view.fragments.WinnerTeam
import java.util.PriorityQueue

interface GameController{
    fun setGameView(gameActivity: GameActivity)
}

class GameControllerImpl(
    private val gameStateModel: GameStateModel,

): GameController {
    private lateinit var gameActivity: GameActivity
    private val abilityPriorityQueue: PriorityQueue<Ability> = PriorityQueue<Ability>(compareBy { it.fetchPriority() })
    private var counter = 0
    private var roundEventsSummary: String = ""
    private var gameLogs: String = ""
    private var gameEnded: Boolean = false

    override fun setGameView(gameActivity: GameActivity) {
        this.gameActivity = gameActivity
        gameActivity.uiEventObservable.subscribe(observer)
        gameActivity.targetPlayersObservable.subscribe(fragmentTargetPlayersObserver)
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

    private val fragmentTargetPlayersObserver: Observer<TargetPlayersSignal> =
        Observer { value ->
            when (value.getTargetPlayersEnum()) {
                TargetPlayersEnum.NoTargetPlayers -> setNoTargetPlayers(value)
                TargetPlayersEnum.WerewolfTeammates -> setWerewolfTeammates(value)
                TargetPlayersEnum.AlivePlayersTarget -> setAlivePlayersTarget(value)
                TargetPlayersEnum.DeadTargets -> setDeadTargets(value)
                TargetPlayersEnum.WerewolfTargets -> setWerewolfTargets(value)
                TargetPlayersEnum.OtherPlayersTarget -> setOtherAlivePlayersTarget(value)
            }
        }

    private val playerObserver: Observer<PlayerSignal> =
        Observer { value ->
            when (value.getPlayer().fetchEvent()){
                PlayerEventEnum.KilledPlayer -> killedPlayer(value.getPlayer())
                PlayerEventEnum.WerewolfKilled -> werewolfKilled(value.getPlayer())
                PlayerEventEnum.JesterWin -> jesterWin(value.getPlayer())
            }
        }

    private val abilityObserver: Observer<AbilitySignal> =
        Observer { value ->
            when (value.getAbility().fetchEvent()){
                AbilityEventEnum.CancelAbility -> cancelAbility(value.getAbility())
                AbilityEventEnum.RevivePlayer -> revivePlayer(value.getAbility().fetchTargetPlayer())
                AbilityEventEnum.RevivePlayerZombie -> revivePlayerZombie(value.getAbility().fetchTargetPlayer())
            }
        }

    private fun setNoTargetPlayers(targetPlayersSignal: TargetPlayersSignal){
        targetPlayersSignal.targetPlayers = listOf()
    }

    private fun setWerewolfTargets(targetPlayersSignal: TargetPlayersSignal){
        targetPlayersSignal.targetPlayers = (gameStateModel.getAliveVillagers()-gameStateModel.getDisguisers().toSet()).shuffled()
    }

    private fun setAlivePlayersTarget(targetPlayersSignal: TargetPlayersSignal){
        targetPlayersSignal.targetPlayers = (gameStateModel.getAliveVillagers()+gameStateModel.getAliveWerewolves()).shuffled()
    }

    private fun setOtherAlivePlayersTarget(targetPlayersSignal: TargetPlayersSignal){
        targetPlayersSignal.targetPlayers = gameStateModel.getAliveVillagers()+gameStateModel.getAliveWerewolves()-listOf(gameStateModel.getAlivePlayers()[counter]).toSet()
    }

    private fun setDeadTargets(targetPlayersSignal: TargetPlayersSignal){
        targetPlayersSignal.targetPlayers = gameStateModel.getDeadPlayers().shuffled()
    }

    private fun setWerewolfTeammates(targetPlayersSignal: TargetPlayersSignal){
        targetPlayersSignal.targetPlayers = (gameStateModel.getAliveWerewolves()+gameStateModel.getDisguisers()).shuffled()
    }

    private fun killedPlayer(player: Player){
        roundEventsSummary += player.fetchPlayerName()+" "+DeathCauseProvider.getDeathCause(player.fetchDeathCause())+"\n"
        gameLogs += player.fetchPlayerName()+" "+DeathCauseProvider.getDeathCause(player.fetchDeathCause())+"\n"
        var result = gameStateModel.getAliveWerewolves().find { it == player }
        if (result != null) {
            gameStateModel.killWerewolf(player)
        } else {
            result = gameStateModel.getAliveVillagers().find { it == player }
            if(result!=null){
                gameStateModel.killVillager(player)
            }
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
        val playerRevived = gameStateModel.revivePlayer(player)
        playerRevived?.playerObservable?.subscribe(playerObserver)
        if(playerRevived!=null){
            roundEventsSummary += player.fetchPlayerName()+" "+MyApp.getAppContext().getString(R.string.was_revived)+"\n"
            gameLogs += player.fetchPlayerName()+" "+MyApp.getAppContext().getString(R.string.was_revived)+"\n"
        }
    }

    private fun revivePlayerZombie(player: Player){
        val playerRevived = gameStateModel.revivePlayerZombie(player)
        playerRevived?.playerObservable?.subscribe(playerObserver)
        if(playerRevived!=null){
            roundEventsSummary += player.fetchPlayerName()+" "+MyApp.getAppContext().getString(R.string.was_revived)+"\n"
            gameLogs += player.fetchPlayerName()+" "+MyApp.getAppContext().getString(R.string.was_revived)+"\n"
        }
    }

    private fun cancelAbility(ability: Ability){
        abilityPriorityQueue.remove(ability)
    }

    private fun initGame(){
        gameStateModel.initGame()
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

    private fun finishRound(){
        queueAbilities()
        resolveAbilities()
        resetDefenseState()
        resetVisitors()
        checkIfGameEnded()
        if(!gameEnded){
            gameActivity.finishRound(roundEventsSummary, gameStateModel.getAlivePlayers())
        }
    }

    private fun queueAbilities(){
        gameStateModel.getAlivePlayers().forEach { player ->
            val usedAbilities = player.fetchUsedAbilities()
            usedAbilities.forEachIndexed { index, ability ->
                ability.playerObservable.subscribe(abilityObserver)
                abilityPriorityQueue.add(ability)
                gameLogs+=player.fetchPlayerName()+" ("+ RoleNameProvider.getRoleName(player.fetchRole())+") "+MyApp.getAppContext().getString(R.string.used)+" "+player.fetchUsedAbilityName(index)+" -> "+ (player.fetchTargetPlayers().firstOrNull()?.fetchPlayerName() ?: "") +"\n"
            }
        }
    }

    private fun resolveAbilities(){
        while (abilityPriorityQueue.isNotEmpty()) {
            val endOfRoundAbility = abilityPriorityQueue.poll()
            endOfRoundAbility?.resolve()
        }
    }

    private fun resetDefenseState(){
        gameStateModel.getAlivePlayers().forEach {
            it.resetDefenseState()
        }
    }

    private fun resetVisitors(){
        gameStateModel.getAlivePlayers().forEach {
            it.resetVisitors()
        }
        gameStateModel.getDeadPlayers().forEach {
            it.resetVisitors()
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
                gameActivity.gameFinished(gameStateModel.getAliveVillagers() + gameStateModel.getDeadVillagers() - gameStateModel.getNeutrals()
                    .toSet(), WinnerTeam.VILLAGERS, gameLogs)
                gameEnded = true
            }
        }
    }

    private fun startNextRound(){
        roundEventsSummary = ""
        gameLogs += "---------------------------------------------------\n"
        checkIfGameEnded()
        if(!gameEnded){
            setCurrentPlayer()
        }
    }
}