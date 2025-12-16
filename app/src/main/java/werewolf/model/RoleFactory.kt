package werewolf.model

import werewolf.model.entities.Player
import werewolf.model.entities.neutrals.Jester
import werewolf.model.entities.villagers.Lightbearer
import werewolf.model.entities.villagers.Detective
import werewolf.model.entities.villagers.Detonator
import werewolf.model.entities.villagers.Disguiser
import werewolf.model.entities.villagers.Elusive
import werewolf.model.entities.villagers.Reviver
import werewolf.model.entities.villagers.Protector
import werewolf.model.entities.villagers.Stalker
import werewolf.model.entities.villagers.Veteran
import werewolf.model.entities.villagers.Vigilante
import werewolf.model.entities.villagers.Villager
import werewolf.model.entities.werewolves.Arsonist
import werewolf.model.entities.werewolves.Necromancer
import werewolf.model.entities.werewolves.Vampire
import werewolf.model.entities.werewolves.Werewolf
import werewolf.model.entities.werewolves.Witch
import werewolf.view.RoleProvider
import kotlin.math.floor

enum class Roles{
    Jester, Villager, Lightbearer, Reviver, Vigilante, Protector, Veteran, Elusive, Detonator, Stalker, Detective, Disguiser, Werewolf, Witch, Arsonist, Vampire, Necromancer, Zombie
}

interface RoleFactory{
    fun getPlayers(): List<Player>
    fun getDisguisers(): List<Disguiser>
}

class RoleFactoryImpl(
    private val gameSettings: GameSettings,
    private val players: MutableList<String>
): RoleFactory {
    private val createdVillagers: MutableList<Player> = mutableListOf()
    private val createdWerewolves: MutableList<Player> = mutableListOf()
    private val createdDisguisers: MutableList<Disguiser> = mutableListOf()

    private val villagerRoles = mutableListOf(Roles.Protector, Roles.Reviver ,Roles.Vigilante ,Roles.Lightbearer,
        Roles.Villager, Roles.Veteran, Roles.Elusive, Roles.Detonator, Roles.Stalker, Roles.Detective, Roles.Disguiser).apply {
        removeAll(RoleProvider.getPremiumRoles())
    }
    private val werewolfRoles = mutableListOf(Roles.Vampire, Roles.Witch, Roles.Necromancer, Roles.Arsonist).apply {
        removeAll(RoleProvider.getPremiumRoles())
    }

    override fun getPlayers(): List<Player> {
        val toReturn = mutableListOf<Player>()
        val cut = floor(players.size / 3.0).toInt()
        toReturn.add(Werewolf(players[0]))
        createdWerewolves.add(toReturn.last())
        for(i in 1 until cut){
            toReturn.add(getRandomWerewolf(players[i]))
        }
        for(i in cut until players.size-1){
            toReturn.add(getRandomVillager(players[i]))
        }
        if(gameSettings.fetchRoleQuantity(Roles.Jester)==0){
            toReturn.add(getRandomVillager(players[players.size-1]))
        }
        else{
            toReturn.add(Jester(players[players.size-1]))
            createdVillagers.add(toReturn.last())
        }
        return toReturn
    }

    override fun getDisguisers(): List<Disguiser> {
        return createdDisguisers
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
            val roleMaxAmount = gameSettings.fetchRoleQuantity(role)
            if(roleMaxAmount>0){
                created = true
                gameSettings.subtractRoleQuantity(role)
                toReturn = createWerewolf(player, role)
            }
            else{
                werewolfRoles.remove(role)
            }
        }
        createdWerewolves.add(toReturn)
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
            val roleMaxAmount = gameSettings.fetchRoleQuantity(role)
            if(roleMaxAmount>0){
                created = true
                gameSettings.subtractRoleQuantity(role)
                toReturn = createVillager(player, role)
            }
            else{
                villagerRoles.remove(role)
            }
        }
        createdVillagers.add(toReturn)
        return toReturn
    }

    private fun createVillager(name: String, role:Roles): Player{
        return when(role){
            Roles.Villager -> Villager(name)
            Roles.Lightbearer -> Lightbearer(name)
            Roles.Reviver -> Reviver(name)
            Roles.Vigilante -> Vigilante(name)
            Roles.Protector -> Protector(name)
            Roles.Veteran -> Veteran(name)
            Roles.Elusive -> Elusive(name)
            Roles.Detonator -> Detonator(name)
            Roles.Stalker -> Stalker(name)
            Roles.Detective -> Detective(name,createdWerewolves,createdVillagers)
            Roles.Disguiser -> createDisguiser(name)
            else -> Villager(name)
        }
    }

    private fun createDisguiser(name: String): Player{
        val disguiser = Disguiser(name)
        createdDisguisers.add(disguiser)
        return disguiser
    }
}