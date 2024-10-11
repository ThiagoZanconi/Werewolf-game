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
    fun addWerewolf(werewolf: Player)
    fun addVillager(villager: Player)
    fun killWerewolf(werewolf: Player)
    fun killVillager(villager: Player)
    fun ascendWerewolf(): Player
    fun reviveVillager(villager: Player): Player
    fun reviveWerewolf(werewolf: Player): Player
}

class GameStateImpl: GameState{
    private lateinit var  alivePlayers: MutableList<Player>
    private val aliveWerewolves: MutableList<Player> = mutableListOf()
    private val deadWerewolves: MutableList<Player> = mutableListOf()
    private val aliveVillagers: MutableList<Player> = mutableListOf()
    private val deadVillagers: MutableList<Player> = mutableListOf()

    override fun initAlivePlayers() {
        this.alivePlayers = (getAliveWerewolves() + getAliveVillagers()).toMutableList()
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

    override fun addWerewolf(werewolf: Player) {
        aliveWerewolves.add(werewolf)
    }

    override fun addVillager(villager: Player) {
        aliveVillagers.add(villager)
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
}