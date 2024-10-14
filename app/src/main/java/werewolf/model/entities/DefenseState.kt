package werewolf.model.entities

import werewolf.model.entities.villagers.Protector

interface DefenseState{

    fun receiveDamage(abstractPlayer: AbstractPlayer, deathCause: DeathCause)
}

class Immune: DefenseState{

    override fun receiveDamage(abstractPlayer: AbstractPlayer, deathCause: DeathCause) {
    }
}

class NoDefense: DefenseState{

    override fun receiveDamage(abstractPlayer: AbstractPlayer, deathCause: DeathCause) {
        abstractPlayer.applyDamage(deathCause)
    }
}

class Protected(private val protector: Protector): DefenseState{

    override fun receiveDamage(abstractPlayer: AbstractPlayer, deathCause: DeathCause) {
        protector.receiveDamage(deathCause)
    }
}