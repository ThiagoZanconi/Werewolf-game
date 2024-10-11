package werewolf.model.entities

import com.example.observer.Observable
import com.example.observer.Subject

interface Ability{
    val playerObservable: Observable<AbilitySignal>
    fun fetchAbilityName(): String
    fun fetchPriority(): Int
    fun fetchTargetPlayer(): Player?
    fun fetchEvent(): AbilityEventEnum
    fun resolve()
    fun cancel()
}

abstract class AbstractAbility(
    protected var targetPlayer: Player?
): Ability{
    private lateinit var event: AbilityEventEnum
    private val onActionSubject = Subject<AbilitySignal>()
    override val playerObservable: Observable<AbilitySignal> = onActionSubject

    override fun fetchTargetPlayer(): Player?{
        return targetPlayer
    }

    override fun fetchEvent(): AbilityEventEnum {
        return event
    }

    override fun cancel(){
        event = AbilityEventEnum.CancelAbility
        onActionSubject.notify(AbilitySignal(this))
    }
}

class WerewolfAttack(targetPlayer: Player?): AbstractAbility(targetPlayer) {
    override fun resolve(){
        targetPlayer?.receiveDamage(DeathCause.MAULED)
    }

    override fun fetchPriority(): Int {
        return 4
    }

    override fun cancel(){

    }

    override fun fetchAbilityName(): String {
        return "Werewolf Attack"
    }

}

class CancelPlayerAbility(targetPlayer: Player?): AbstractAbility(targetPlayer) {
    override fun resolve() {
        targetPlayer?.cancelAbility()
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
): AbstractAbility(targetPlayer) {
    override fun resolve() {
        targetPlayer?.defineDefenseState(Immune())
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
): AbstractAbility(targetPlayer) {
    override fun resolve() {
        targetPlayer?.signalEvent(PlayerEventEnum.RevivedPlayer)
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
): AbstractAbility(targetPlayer){

    override fun resolve() {
        targetPlayer?.receiveDamage(DeathCause.SHOT)
    }

    override fun fetchAbilityName(): String {
        return "Shot"
    }

    override fun fetchPriority(): Int {
        return 2
    }
}