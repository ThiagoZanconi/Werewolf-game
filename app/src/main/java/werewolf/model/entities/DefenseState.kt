package werewolf.model.entities

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

class Protected(private val protector: Player): DefenseState{

    override fun receiveDamage(abstractPlayer: AbstractPlayer, deathCause: DeathCause) {
        protector.receiveDamage(deathCause)
    }
}