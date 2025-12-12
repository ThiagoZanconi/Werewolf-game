package werewolf.model

import werewolf.model.entities.Player
import werewolf.model.entities.werewolves.Zombie
import werewolf.model.repository.Repository
import werewolf.model.repository.RepositoryImpl
import werewolf.view.GameActivity
import werewolf.view.MyApp
import kotlin.math.floor

interface GameStateModel{
    fun setGameView(gameActivity: GameActivity)
    fun getAlivePlayers(): List<Player>
    fun getAliveVillagers(): List<Player>
    fun getAliveWerewolves(): List<Player>
    fun getDeadVillagers(): List<Player>
    fun getDeadWerewolves(): List<Player>
    fun getDeadPlayers(): List<Player>
    fun getNeutrals(): List<Player>
    fun getDisguisers(): List<Player>
    fun initGame()
    fun killVillager(player: Player)
    fun killWerewolf(player: Player)
    fun ascendWerewolf(): Player
    fun revivePlayer(player: Player): Player?
    fun revivePlayerZombie(player: Player): Player?
}

class GameStateModelImpl: GameStateModel{

    private val repository: Repository = RepositoryImpl(MyApp.getAppContext())
    private val gameState: GameState = GameStateImpl()
    private val gameSettings: GameSettings = GameSettingsImpl
    private lateinit var roleFactory: RoleFactory
    private lateinit var gameActivity: GameActivity

    override fun setGameView(gameActivity: GameActivity) {
        this.gameActivity = gameActivity
    }

    override fun initGame() {
        //createProfiles(players)
        val playerPositionMap: MutableMap<String, Int> = mutableMapOf()
        for(index in 0 until gameSettings.fetchPlayers().size){
            playerPositionMap[gameSettings.fetchPlayers()[index]] = index
        }
        gameSettings.fetchPlayers().shuffle()
        roleFactory = RoleFactoryImpl(gameSettings,gameSettings.fetchPlayers())
        val playersAssigned = roleFactory.getPlayers()
        val cut = floor(gameSettings.fetchPlayers().size / 3.0).toInt()
        for(i in 0 until cut){
            addWerewolf(playersAssigned[i])
        }
        for(i in cut until gameSettings.fetchPlayers().size-1){
            addVillager(playersAssigned[i])
        }
        if(gameSettings.fetchRoleQuantity(Roles.Jester)==0){
            addVillager(playersAssigned[gameSettings.fetchPlayers().size-1])
        }
        else{
            addNeutral(playersAssigned[gameSettings.fetchPlayers().size-1])
        }
        gameState.initAlivePlayers(playerPositionMap)
        //printProfiles()
    }

    override fun getAlivePlayers(): List<Player> {
        return gameState.getAlivePlayers()
    }

    override fun getAliveVillagers(): List<Player> {
        return gameState.getAliveVillagers()
    }

    override fun getAliveWerewolves(): List<Player> {
        return gameState.getAliveWerewolves()
    }

    override fun getDeadVillagers(): List<Player> {
        return gameState.getDeadVillagers()
    }

    override fun getDeadWerewolves(): List<Player> {
        return gameState.getDeadWerewolves()
    }

    override fun getDeadPlayers(): List<Player> {
        return gameState.getDeadVillagers()+gameState.getDeadWerewolves()
    }

    override fun getNeutrals(): List<Player> {
        return gameState.getNeutrals()
    }

    override fun getDisguisers(): List<Player> {
        return roleFactory.getDisguisers()
    }

    override fun killVillager(player: Player) {
        gameState.killVillager(player)
    }

    override fun killWerewolf(player: Player) {
        gameState.killWerewolf(player)
    }

    override fun ascendWerewolf(): Player {
        return gameState.ascendWerewolf()
    }

    override fun revivePlayer(player: Player): Player? {
        var index = getDeadVillagers().indexOf(player)
        if(index!=-1){
            return gameState.reviveVillager(player)
        }
        index = getDeadWerewolves().indexOf(player)
        if(index!=-1){
            return gameState.reviveWerewolf(player)
        }
        return null
    }

    override fun revivePlayerZombie(player: Player): Player? {
        val playerRemoved = gameState.removeDeadPlayer(player)
        if(playerRemoved){
            val zombiePlayer = Zombie(player.fetchPlayerName())
            gameState.addWerewolf(zombiePlayer)
            return zombiePlayer
        }
        else{
            return null
        }
    }

    private fun addWerewolf(player: Player) {
        gameState.addWerewolf(player)
    }

    private fun addVillager(player: Player) {
        gameState.addVillager(player)
    }

    private fun addNeutral(player: Player) {
        gameState.addVillager(player)
        gameState.addNeutral(player)
    }

    private fun createProfiles(players: List<String>){
        players.forEach {
            repository.createProfile(it)
        }
    }

    private fun printProfiles(){
        repository.getProfiles().forEach {
            println("Name: "+it.name+", Jester Wins: "+it.jesterWins+", Werewolf Wins: "+it.werewolfWins+", Villager Wins: "+it.villagerWins)
        }
    }
}