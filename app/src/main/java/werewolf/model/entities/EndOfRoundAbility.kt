package werewolf.model.entities

interface EndOfRoundAbility{
    fun fetchAbilityName(): String
    fun fetchPriority(): Int
    fun fetchTargetPlayer(): Player?
    fun resolve()
    fun nullify()
}

abstract class AbstractEndOfRoundAbility(
    protected var targetPlayer: Player?
): EndOfRoundAbility{
    protected var nullified = false

    override fun fetchTargetPlayer(): Player?{
        return targetPlayer
    }

    override fun nullify(){
        nullified = true
    }
}

class WerewolfAttack(targetPlayer: Player?): AbstractEndOfRoundAbility(targetPlayer) {
    override fun resolve(){
        targetPlayer?.receiveDamage(DeathCause.MAULED)
    }

    override fun fetchPriority(): Int {
        return 4
    }

    override fun nullify(){

    }

    override fun fetchAbilityName(): String {
        return "Werewolf Attack"
    }

}

class CancelPlayerAbility(targetPlayer: Player?): AbstractEndOfRoundAbility(targetPlayer) {
    override fun resolve() {
        targetPlayer?.nullifyAbility()
    }

    override fun fetchAbilityName(): String {
        return "Null Player Ability"
    }

    override fun fetchPriority(): Int {
        return 1
    }

}

class Shield(
    targetPlayer: Player?
): AbstractEndOfRoundAbility(targetPlayer) {
    override fun resolve() {
        if(!nullified){
            targetPlayer?.defineDefenseState(Immune())
        }
    }

    override fun fetchAbilityName(): String {
        return "Shield"
    }

    override fun fetchPriority(): Int {
        return 2
    }
}

class ReviveSpell(
    targetPlayer: Player?
): AbstractEndOfRoundAbility(targetPlayer) {
    override fun resolve() {
        if(!nullified){
            targetPlayer?.signalEvent(PlayerEventEnum.RevivedPlayer)
        }
    }

    override fun fetchAbilityName(): String {
        return "Revive Spell"
    }

    override fun fetchPriority(): Int {
        return 2
    }
}

class Shot(
    targetPlayer: Player?
): AbstractEndOfRoundAbility(targetPlayer){

    override fun resolve() {
        if(!nullified){
            targetPlayer?.receiveDamage(DeathCause.SHOT)
        }
    }

    override fun fetchAbilityName(): String {
        return "Shot"
    }

    override fun fetchPriority(): Int {
        return 2
    }
}

