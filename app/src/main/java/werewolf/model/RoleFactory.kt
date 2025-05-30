package werewolf.model

import werewolf.model.entities.Player
import werewolf.model.entities.neutrals.Jester
import werewolf.model.entities.villagers.Cleric
import werewolf.model.entities.villagers.Elusive
import werewolf.model.entities.villagers.Priest
import werewolf.model.entities.villagers.Protector
import werewolf.model.entities.villagers.Veteran
import werewolf.model.entities.villagers.Vigilante
import werewolf.model.entities.villagers.Villager
import werewolf.model.entities.werewolves.Arsonist
import werewolf.model.entities.werewolves.Necromancer
import werewolf.model.entities.werewolves.Vampire
import werewolf.model.entities.werewolves.Werewolf
import werewolf.model.entities.werewolves.Witch
import werewolf.view.settings.RoleQuantitySettings
import kotlin.math.floor

enum class Roles{
    Werewolf, Witch, Arsonist, Vampire, Necromancer, Zombie, Villager, Cleric, Priest, Vigilante, Protector, Veteran, Elusive, Jester
}

interface RoleFactory{
    fun getPlayers(): List<Player>
}

class RoleFactoryImpl(
    private val roleQuantitySettings: RoleQuantitySettings,
    private val players: MutableList<String>
): RoleFactory {

    private val villagerRoles = mutableListOf(Roles.Protector, Roles.Priest ,Roles.Vigilante ,Roles.Cleric, Roles.Villager, Roles.Veteran, Roles.Elusive)
    private val werewolfRoles = mutableListOf(Roles.Vampire, Roles.Witch, Roles.Necromancer, Roles.Arsonist)

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
        if(roleQuantitySettings.getRoleQuantity(Roles.Jester)==0){
            toReturn.add(getRandomVillager(players[players.size-1]))
        }
        else{
            toReturn.add(Jester(players[players.size-1]))
        }
        return toReturn
    }

    private fun getRandomWerewolf(player: String): Player{
        var toReturn: Player = Werewolf(player)
        var created = false
        while(!created){
            if(werewolfRoles.isEmpty()){
                toReturn = createWerewolf(player,Roles.Witch)
                break
            }
            val role = werewolfRoles.random()
            val roleMaxAmount = roleQuantitySettings.getRoleQuantity(role)
            if(roleMaxAmount>0){
                created = true
                roleQuantitySettings.subtractRoleQuantity(role)
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
            Roles.Necromancer -> Necromancer(name)
            Roles.Arsonist -> Arsonist(name)
            else -> Witch(name)
        }
    }

    private fun getRandomVillager(player: String): Player{
        var toReturn: Player = Villager(player)
        var created = false
        while(!created){
            if(villagerRoles.isEmpty()){
                toReturn = createVillager(player,Roles.Villager)
                break
            }
            val role = villagerRoles.random()
            val roleMaxAmount = roleQuantitySettings.getRoleQuantity(role)
            if(roleMaxAmount>0){
                created = true
                roleQuantitySettings.subtractRoleQuantity(role)
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
            Roles.Veteran -> Veteran(name)
            Roles.Elusive -> Elusive(name)
            else -> Villager(name)
        }
    }
}