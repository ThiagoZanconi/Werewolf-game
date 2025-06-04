package werewolf.model.entities

import werewolf.model.entities.villagers.Protector

interface DefenseState{

    fun receiveDamage(abstractPlayer: AbstractPlayer, werewolfAttackAbility: WerewolfAttackAbility)
    fun receiveDamage(abstractPlayer: AbstractPlayer, villagerAttackAbility: VillagerAttackAbility)
}

class Immune: DefenseState{

    override fun receiveDamage(abstractPlayer: AbstractPlayer, werewolfAttackAbility: WerewolfAttackAbility) {
    }

    override fun receiveDamage(abstractPlayer: AbstractPlayer, villagerAttackAbility: VillagerAttackAbility) {
    }
}

class NoDefense: DefenseState{

    override fun receiveDamage(abstractPlayer: AbstractPlayer, werewolfAttackAbility: WerewolfAttackAbility) {
        abstractPlayer.applyDamage(werewolfAttackAbility.fetchDeathCause())
    }

    override fun receiveDamage(abstractPlayer: AbstractPlayer, villagerAttackAbility: VillagerAttackAbility) {
        abstractPlayer.applyDamage(villagerAttackAbility.fetchDeathCause())
    }
}

class Protected(private val protector: Protector): DefenseState{

    override fun receiveDamage(abstractPlayer: AbstractPlayer, werewolfAttackAbility: WerewolfAttackAbility) {
        protector.receiveAttack(werewolfAttackAbility)
    }

    override fun receiveDamage(abstractPlayer: AbstractPlayer, villagerAttackAbility: VillagerAttackAbility) {
        protector.receiveAttack(villagerAttackAbility)
    }
}