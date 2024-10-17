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

enum class Roles{
    Werewolf, Witch, Vampire, Villager, Cleric, Priest, Vigilante, Protector, Jester
}

interface RoleFactory{
    fun getPlayers(): List<Player>
}

class RoleFactoryImpl(
    private val settings: File,
    private val players: MutableList<String>
): RoleFactory {

    private val villagerRoles = mutableListOf(Roles.Protector, Roles.Priest ,Roles.Vigilante ,Roles.Cleric, Roles.Villager)
    private val werewolfRoles = mutableListOf(Roles.Vampire, Roles.Witch)

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
            if(werewolfRoles.isEmpty()){
                toReturn = createWerewolf(player,Roles.Witch)
                break
            }
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
            if(villagerRoles.isEmpty()){
                println("Entre")
                toReturn = createVillager(player,Roles.Villager)
                break
            }
            val role = villagerRoles.random()
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
}