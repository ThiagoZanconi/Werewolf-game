package werewolf.model

import werewolf.model.entities.Player
import werewolf.view.GameActivity
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
    fun initGame(players: MutableList<String>)
    fun killVillager(player: Player)
    fun killWerewolf(player: Player)
    fun ascendWerewolf(): Player
    fun revivePlayer(player: Player): Player?
}

class GameStateModelImpl(
    private val rolesQuantityRestrictions: RolesQuantityRestrictions
): GameStateModel{
    private lateinit var gameActivity: GameActivity
    private val gameState: GameState = GameStateImpl()
    private lateinit var roleFactory: RoleFactory

    override fun setGameView(gameActivity: GameActivity) {
        this.gameActivity = gameActivity
    }

    override fun initGame(players: MutableList<String>) {
        players.shuffle()
        roleFactory = RoleFactoryImpl(rolesQuantityRestrictions,players)
        val playersAssigned = roleFactory.getPlayers()
        val cut = floor(players.size / 3.0).toInt()
        for(i in 0 until cut){
            addWerewolf(playersAssigned[i])
        }
        for(i in cut until players.size-1){
            addVillager(playersAssigned[i])
        }
        addNeutral(playersAssigned[players.size-1])
        gameState.initAlivePlayers()
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
}