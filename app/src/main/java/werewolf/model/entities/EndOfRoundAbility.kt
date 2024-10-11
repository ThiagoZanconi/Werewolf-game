package werewolf.model.entities

interface EndOfRoundAbility{
    fun getAbilityName(): String
    fun getPriority(): Int
    fun resolve()
    fun nullify()
}

abstract class AbstractEndOfRoundAbility: EndOfRoundAbility{
    protected var nullified = false

    override fun nullify(){
        nullified = true
    }
}

class NullAbility: AbstractEndOfRoundAbility() {
    override fun resolve(){

    }

    override fun getAbilityName(): String {
        return "No Ability"
    }

    override fun getPriority(): Int {
        return 0
    }

}

class WerewolfAttack(private val targetPlayer: Player): AbstractEndOfRoundAbility() {
    override fun resolve(){
        targetPlayer.receiveDamage(DeathCause.MAULED)
    }

    override fun getPriority(): Int {
        return 4
    }

    override fun nullify(){

    }

    override fun getAbilityName(): String {
        return "Werewolf Attack"
    }

}

class NullPlayerAbility(private val targetPlayer: Player): AbstractEndOfRoundAbility() {
    override fun resolve() {
        targetPlayer.nullifyAbility()
    }

    override fun getAbilityName(): String {
        return "Null Player Ability"
    }

    override fun getPriority(): Int {
        return 1
    }

}

class Shield(
    private val targetPlayer: Player
): AbstractEndOfRoundAbility() {
    override fun resolve() {
        if(!nullified){
            targetPlayer.defineDefenseState(Immune())
        }
    }

    override fun getAbilityName(): String {
        return "Shield"
    }

    override fun getPriority(): Int {
        return 2
    }
}

class ReviveSpell(
    private val targetPlayer: Player
): AbstractEndOfRoundAbility() {
    override fun resolve() {
        if(!nullified){
            targetPlayer.signalEvent(PlayerEventEnum.RevivedPlayer)
        }
    }

    override fun getAbilityName(): String {
        return "Revive Spell"
    }

    override fun getPriority(): Int {
        return 2
    }
}

class Shot(
        private val targetPlayer: Player
): AbstractEndOfRoundAbility(){

    override fun resolve() {
        if(!nullified){
            targetPlayer.receiveDamage(DeathCause.SHOT)
        }
    }

    override fun getAbilityName(): String {
        return "Shot"
    }

    override fun getPriority(): Int {
        return 2
    }
}

