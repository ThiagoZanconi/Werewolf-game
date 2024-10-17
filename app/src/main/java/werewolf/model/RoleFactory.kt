package werewolf.model

import werewolf.model.entities.Player
import werewolf.model.entities.neutrals.Jester
import werewolf.model.entities.villagers.Cleric
import werewolf.model.entities.villagers.Priest
import werewolf.model.entities.villagers.Protector
import werewolf.model.entities.villagers.Vigilante
import werewolf.model.entities.villagers.Villager
import werewolf.model.entities.werewolves.Vampire
import werewolf.model.entities.werewolves.Werewolf
import werewolf.model.entities.werewolves.Witch
import java.io.File
import kotlin.math.floor
import kotlin.random.Random

enum class Roles{
    Werewolf, Witch, Vampire, Villager, Cleric, Priest, Vigilante, Protector, Jester
}

interface RoleFactory{
    fun getWerewolf(name: String): Player
    fun getPlayers(): List<Player>
    fun getVillager(name: String): Player
    fun getVillagers(): List<Player>
    fun getNeutral(name: String): Player
}

class RoleFactoryImpl(
    private val settings: File,
    private val players: MutableList<String>
): RoleFactory {
    private var werewolfCreated = false
    private val WEREWOLF_ROLES = 2
    private val VILLAGER_ROLES = 5
    private val NEUTRAL_ROLES = 1

    private val villagerRoles = mutableListOf(Roles.Protector, Roles.Priest ,Roles.Vigilante ,Roles.Cleric, Roles.Villager)
    private val werewolfRoles = mutableListOf(Roles.Vampire, Roles.Witch)

    override fun getWerewolf(name: String): Player {
        return if (!werewolfCreated) {
            werewolfCreated=true
            createWerewolf(name)
        }
        else{
            createRandomWerewolf(name)
        }
    }

    override fun getPlayers(): List<Player> {
        val toReturn = mutableListOf<Player>()
        val cut = floor(players.size / 3.0).toInt()
        toReturn.add(Werewolf(players[0]))
        for(i in 1 until cut){
            toReturn.add(getRandomWerewolf(players[i]))
        }
        for(i in cut until players.size-1){
            toReturn.add(getRandomVillager(players[i]))
        }
        toReturn.add(Jester(players[players.size-1]))
        return toReturn
    }

    private fun getRandomWerewolf(player: String): Player{
        var toReturn: Player = Werewolf(player)
        var created = false
        val lines = settings.readLines().toMutableList()
        while(!created){
            val role = werewolfRoles.random()
            println("Role factory max amount: "+lines[role.ordinal].toInt())
            var roleMaxAmount = lines[role.ordinal].toInt()
            if(roleMaxAmount>0){
                created = true
                roleMaxAmount--
                lines[role.ordinal] = roleMaxAmount.toString()
                settings.writeText(lines.joinToString("\n"))
                toReturn = createWerewolf(player, role)
            }
            else{
                werewolfRoles.remove(role)
            }
            if(werewolfRoles.isEmpty()){
                toReturn = createWerewolf(player,Roles.Witch)
                created = true
            }
        }
        return toReturn
    }

    private fun createWerewolf(name: String, role:Roles): Player{
        return when(role){
            Roles.Witch -> Witch(name)
            Roles.Vampire -> Vampire(name)
            else -> Witch(name)
        }
    }

    private fun getRandomVillager(player: String): Player{
        var toReturn: Player = Villager(player)
        var created = false
        val lines = settings.readLines().toMutableList()
        while(!created){
            val role = villagerRoles.random()
            println("Role factory max amount: "+lines[role.ordinal].toInt())
            var roleMaxAmount = lines[role.ordinal].toInt()
            if(roleMaxAmount>0){
                created = true
                roleMaxAmount--
                lines[role.ordinal] = roleMaxAmount.toString()
                settings.writeText(lines.joinToString("\n"))
                toReturn = createVillager(player, role)
            }
            else{
                villagerRoles.remove(role)
            }
            if(werewolfRoles.isEmpty()){
                toReturn = createWerewolf(player,Roles.Witch)
                created = true
            }
        }
        return toReturn
    }

    private fun createVillager(name: String, role:Roles): Player{
        return when(role){
            Roles.Villager -> Villager(name)
            Roles.Cleric -> Cleric(name)
            Roles.Priest -> Priest(name)
            Roles.Vigilante -> Vigilante(name)
            Roles.Protector -> Protector(name)
            else -> Villager(name)
        }
    }

    override fun getVillager(name: String): Player {
        return when (Random.nextInt(VILLAGER_ROLES)) {
            0 -> createVillager(name)
            1 -> createCleric(name)
            2 -> createPriest(name)
            3 -> createVigilante(name)
            4 -> createProtector(name)
            else -> createVillager(name)
        }
    }

    override fun getVillagers(): List<Player> {
        TODO("Not yet implemented")
    }

    override fun getNeutral(name: String): Player{
        return when (Random.nextInt(NEUTRAL_ROLES)) {
            0 -> createJester(name)
            else -> createJester(name)
        }
    }

    private fun createWerewolf(name: String): Player {
        return Werewolf(name)
    }

    private fun createRandomWerewolf(name: String): Player{
        return when (Random.nextInt(WEREWOLF_ROLES)) {
            0 -> createWitch(name)
            1 -> createVampire(name)
            else -> createVampire(name)
        }
    }

    private fun createWitch(name: String): Player {
        return Witch(name)
    }

    private fun createVampire(name: String): Player {
        return Vampire(name)
    }

    private fun createVillager(name: String): Player{
        return Villager(name)
    }

    private fun createCleric(name: String): Player{
        return Cleric(name)
    }

    private fun createJester(name: String): Player{
        return Jester(name)
    }

    private fun createPriest(name: String): Player{
        return Priest(name)
    }

    private fun createVigilante(name: String): Player{
        return Vigilante(name)
    }

    private fun createProtector(name: String): Player{
        return Protector(name)
    }
}