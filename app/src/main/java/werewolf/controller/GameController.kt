package werewolf.controller

import com.example.observer.Observer
import kotlinx.coroutines.CoroutineScope
import org.json.JSONArray
import org.json.JSONObject
import werewolf.model.GameStateModel
import werewolf.model.entities.Ability
import werewolf.model.entities.AbilityEventEnum
import werewolf.model.entities.AbilitySignal
import werewolf.model.entities.Hang
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.PlayerSignal
import werewolf.model.entities.TargetPlayersEnum
import werewolf.view.GameActivity
import werewolf.view.GameUiEvent
import werewolf.view.GameUiEventSignal
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.RoleProvider
import werewolf.view.fragments.WinnerTeam
import java.util.PriorityQueue

interface GameController{
    fun setGameView(gameActivity: GameActivity, scope: CoroutineScope)
}

enum class GameState{
    OnGoing, EndedJesterWin, Ended
}

class GameControllerImpl(
    private val gameStateModel: GameStateModel,

    ): GameController {
    private lateinit var gameActivity: GameActivity
    private lateinit var currentPlayer: Player
    private lateinit var scope: CoroutineScope
    private val abilityPriorityQueue: PriorityQueue<Ability> = PriorityQueue<Ability>(compareBy { it.fetchPriority() })
    private var counter = 0
    private var roundEventsSummary: String = ""
    private var round = 1
    private var gameLogs: String = "<--Round $round-->\n"
    private var gameState: GameState = GameState.OnGoing

    override fun setGameView(gameActivity: GameActivity, scope: CoroutineScope) {
        this.scope = scope
        this.gameActivity = gameActivity
        this.gameActivity.uiEventObservable.subscribe(observer)
        initGame()
    }

    private val observer: Observer<GameUiEventSignal> =
        Observer { value ->
            when (value.getEvent()) {
                GameUiEvent.ConfirmAction -> confirmAction()
                GameUiEvent.StartTurn -> startTurn()
                GameUiEvent.StartNextRound -> startNextRound()
                GameUiEvent.AbilityUsed -> {
                    abilityUsed(value.getJson())
                    confirmAction()
                }
                GameUiEvent.HangedPlayer -> hangedPlayer(value.getJson())
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

    private fun abilityUsed(json: JSONObject){
        val player = getPlayer(json.getString("PlayerName"))
        val jsonArray = json.getJSONArray("TargetPlayers")
        val targetPlayersNames = List(jsonArray.length()) { i ->
            jsonArray.getString(i)
        }
        val targetPlayers = mutableListOf<Player>()
        targetPlayersNames.forEach {
            targetPlayers.add(getPlayer(it))
        }

        player.notifyAbilityUsed(targetPlayers)
    }

    private fun killedPlayer(player: Player){
        roundEventsSummary += player.fetchPlayerName()+" "+ DeathCauseProvider.getDeathCause(player.fetchDeathCause())+"\n"
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
        val json = JSONObject()
        json.put("Event", Events.GameFinished)
        json.put("GameLogs",gameLogs)
        json.put("WinnerTeam", WinnerTeam.JESTER)
        json.put("WinnerPlayerList", JSONArray(listOf(player.fetchPlayerName())))
        gameState = GameState.EndedJesterWin
        gameActivity.gameFinished(json)
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
        gameStateModel.initGame(scope)
        val players = gameStateModel.getAlivePlayers()
        players.forEach {
                player -> player.playerObservable.subscribe(playerObserver)
        }
        setCurrentPlayer()
    }

    private fun getPlayer(playerName: String): Player{
        return (gameStateModel.getAlivePlayers()+gameStateModel.getDeadPlayers()).find { it.fetchPlayerName() == playerName }!!
    }

    private fun setCurrentPlayer(){
        val player = gameStateModel.getAlivePlayers()[counter]
        currentPlayer = player
        gameActivity.setCurrentPlayer(currentPlayer.fetchPlayerName())
    }

    private fun startTurn(){
        currentPlayer.turnSetUp()
        val json = getPlayerTurnJson(currentPlayer)
        gameActivity.startTurn(json)
    }

    private fun getPlayerTurnJson(player: Player): JSONObject{
        val targetPlayerOptions = player.fetchTargetPlayersOptions()
        val targetPlayers = getTargetPlayers(targetPlayerOptions)
        player.definePossibleTargetPlayers(targetPlayers)
        player.defineTeammates(getWerewolfTeammates()) //Werewolf Teammates
        return player.json()
    }

    private fun getTargetPlayers(targetPlayerOptions: TargetPlayersEnum): List<String>{
        return when (targetPlayerOptions) {
            TargetPlayersEnum.NoTargetPlayers -> getNoTargetPlayers()
            TargetPlayersEnum.WerewolfTeammates -> getWerewolfTeammates()
            TargetPlayersEnum.AlivePlayersTarget -> getAlivePlayersTarget()
            TargetPlayersEnum.DeadTargets -> getDeadTargets()
            TargetPlayersEnum.WerewolfTargets -> getWerewolfTargets()
            TargetPlayersEnum.OtherPlayersTarget -> getOtherAlivePlayersTarget()
        }
    }

    private fun getNoTargetPlayers(): List<String>{
        return listOf()
    }

    private fun getWerewolfTargets(): List<String> {
        return (gameStateModel.getAliveVillagers() - gameStateModel.getDisguisers().toSet()).shuffled().map { it.fetchPlayerName() }
    }

    private fun getAlivePlayersTarget(): List<String>{
        return (gameStateModel.getAliveVillagers()+gameStateModel.getAliveWerewolves()).shuffled().map { it.fetchPlayerName() }
    }

    private fun getOtherAlivePlayersTarget(): List<String>{
        return (gameStateModel.getAliveVillagers()+gameStateModel.getAliveWerewolves()-listOf(currentPlayer).toSet()).shuffled().map { it.fetchPlayerName() }
    }

    private fun getDeadTargets(): List<String>{
        return gameStateModel.getDeadPlayers().shuffled().map { it.fetchPlayerName() }
    }

    private fun getWerewolfTeammates(): List<String>{
        return (gameStateModel.getAliveWerewolves()+gameStateModel.getDisguisers()).shuffled().map { it.fetchPlayerName() }
    }

    private fun finishRound(){
        counter = 0
        queueAbilities()
        resolveAbilities()
        resetDefenseState()
        resetVisitors()
        checkIfGameEnded()
        if(gameState == GameState.OnGoing){
            gameActivity.finishRound(roundEventsSummary, gameStateModel.getAlivePlayers())
        }
    }

    private fun queueAbilities(){
        gameStateModel.getAlivePlayers().forEach { player ->
            val usedAbilities = player.fetchUsedAbilities()
            usedAbilities.forEach { ability ->
                ability.playerObservable.subscribe(abilityObserver)
                abilityPriorityQueue.add(ability)
            }
            if(usedAbilities.isNotEmpty()){
                gameLogs+="${player.fetchPlayerName()} " +
                        "(${RoleProvider.getRoleName(player.fetchRole())}) " +
                        "${MyApp.getAppContext().getString(R.string.used)} " +
                        "${player.fetchUsedAbilityName(0)} -> " +
                        "${player.fetchTargetPlayers().map{it.fetchPlayerName()}}\n"
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

    private fun checkIfGameEnded() {
        val json = JSONObject()
        json.put("Event", Events.GameFinished)
        json.put("GameLogs",gameLogs)
        when {
            gameStateModel.getAliveVillagers().isEmpty() && gameStateModel.getAliveWerewolves().isEmpty() -> {
                val winnerPlayerList = listOf<Player>().map{it.fetchPlayerName()}
                json.put("WinnerTeam", WinnerTeam.DRAW)
                json.put("WinnerPlayerList", JSONArray(winnerPlayerList))
                gameState = GameState.Ended
            }
            gameStateModel.getAliveVillagers().isEmpty() -> {
                val winnerTeamList = gameStateModel.getAliveWerewolves() + gameStateModel.getDeadWerewolves()
                json.put("WinnerTeam", WinnerTeam.WEREWOLVES)
                json.put("WinnerPlayerList", JSONArray(winnerTeamList.map{it.fetchPlayerName()}))
                gameState = GameState.Ended
            }
            gameStateModel.getAliveWerewolves().isEmpty() -> {
                val winnerTeamList = gameStateModel.getAliveVillagers() + gameStateModel.getDeadVillagers() - gameStateModel.getNeutrals().toSet()
                json.put("WinnerTeam", WinnerTeam.VILLAGERS)
                json.put("WinnerPlayerList", JSONArray(winnerTeamList.map{it.fetchPlayerName()}))
                gameState = GameState.Ended
            }
        }
        if(gameState == GameState.Ended){
            gameActivity.gameFinished(json)
        }

    }

    private fun startNextRound(){
        checkIfGameEnded()
        if(gameState == GameState.OnGoing){
            roundEventsSummary = ""
            round++
            gameLogs += "<--Round $round-->\n"
            setCurrentPlayer()
        }
    }

    private fun hangedPlayer(json: JSONObject){
        val targetPlayerName = json.getString("TargetPlayer")
        val targetPlayer = gameStateModel.getAlivePlayers().find { it.fetchPlayerName() == targetPlayerName }
        targetPlayer?.receiveAttack(Hang(targetPlayer))
        startNextRound()
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

}