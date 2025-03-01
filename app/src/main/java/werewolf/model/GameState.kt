package werewolf.model

import werewolf.model.entities.Player
import werewolf.model.entities.werewolves.Werewolf
import werewolf.model.entities.werewolves.Witch

interface GameState{
    fun initAlivePlayers()
    fun getAlivePlayers(): List<Player>
    fun getAliveWerewolves(): List<Player>
    fun getAliveVillagers(): List<Player>
    fun getDeadWerewolves(): List<Player>
    fun getDeadVillagers(): List<Player>
    fun getNeutrals(): List<Player>
    fun addWerewolf(werewolf: Player)
    fun addVillager(villager: Player)
    fun addNeutral(player: Player)
    fun killWerewolf(werewolf: Player)
    fun killVillager(villager: Player)
    fun ascendWerewolf(): Player
    fun reviveVillager(villager: Player): Player
    fun reviveWerewolf(werewolf: Player): Player
    fun removePlayer(player: Player): Boolean
}

class GameStateImpl: GameState{
    private val alivePlayers: MutableList<Player> = mutableListOf()
    private val aliveWerewolves: MutableList<Player> = mutableListOf()
    private val deadWerewolves: MutableList<Player> = mutableListOf()
    private val aliveVillagers: MutableList<Player> = mutableListOf()
    private val deadVillagers: MutableList<Player> = mutableListOf()
    private val neutrals: MutableList<Player> = mutableListOf()

    override fun initAlivePlayers() {
        alivePlayers.shuffle()
    }

    override fun getAlivePlayers(): List<Player> {
        return alivePlayers
    }

    override fun getAliveWerewolves(): List<Player> {
        return aliveWerewolves
    }

    override fun getAliveVillagers(): List<Player> {
        return aliveVillagers
    }

    override fun getDeadWerewolves(): List<Player> {
        return deadWerewolves
    }

    override fun getDeadVillagers(): List<Player> {
        return deadVillagers
    }

    override fun getNeutrals(): List<Player> {
        return neutrals
    }

    override fun addWerewolf(werewolf: Player) {
        alivePlayers.add(werewolf)
        aliveWerewolves.add(werewolf)
    }

    override fun addVillager(villager: Player) {
        alivePlayers.add(villager)
        aliveVillagers.add(villager)
    }

    override fun addNeutral(player: Player) {
        neutrals.add(player)
    }

    override fun killWerewolf(werewolf: Player) {
        alivePlayers.remove(werewolf)
        aliveWerewolves.remove(werewolf)
        deadWerewolves.add(werewolf)
    }

    override fun killVillager(villager: Player) {
        alivePlayers.remove(villager)
        aliveVillagers.remove(villager)
        deadVillagers.add(villager)
    }

    override fun ascendWerewolf(): Player {
        val index = alivePlayers.indexOf(aliveWerewolves[0])
        val werewolf: Player = Werewolf(aliveWerewolves[0].fetchPlayerName())
        werewolf.defineTargetPlayers(aliveVillagers)
        aliveWerewolves[0] = werewolf
        alivePlayers[index] = werewolf
        return werewolf
    }

    override fun reviveVillager(villager: Player): Player {
        deadVillagers.remove(villager)
        aliveVillagers.add(villager)
        alivePlayers.add(villager)
        return villager
    }

    override fun reviveWerewolf(werewolf: Player): Player {
        deadWerewolves.remove(werewolf)
        val witch: Player = Witch(werewolf.fetchPlayerName())
        witch.defineTargetPlayers(aliveVillagers)
        aliveWerewolves.add(witch)
        alivePlayers.add(witch)
        return witch
    }

    override fun removePlayer(player: Player): Boolean {
        val werewolfRemoved = deadWerewolves.remove(player)
        val villagerRemoved = deadVillagers.remove(player)
        return werewolfRemoved || villagerRemoved
    }
}