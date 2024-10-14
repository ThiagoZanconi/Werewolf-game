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
import kotlin.random.Random

interface RoleFactory{
    fun getWerewolf(name: String): Player
    fun getVillager(name: String): Player
    fun getNeutral(name: String): Player
}

class RoleFactoryImpl: RoleFactory {
    private var werewolfCreated = false
    private val WEREWOLF_ROLES = 2
    private val VILLAGER_ROLES = 5
    private val NEUTRAL_ROLES = 1

    override fun getWerewolf(name: String): Player {
        return if (!werewolfCreated) {
            werewolfCreated=true
            createWerewolf(name)
        }
        else{
            createRandomWerewolf(name)
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