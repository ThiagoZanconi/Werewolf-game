package werewolf.model.entities

import com.example.observer.Observable
import com.example.observer.Subject
import werewolf.view.MyApp
import werewolf.view.R

interface Ability{
    val playerObservable: Observable<AbilitySignal>
    fun fetchAbilityName(): String
    fun fetchPriority(): Int
    fun fetchTargetPlayer(): Player
    fun fetchEvent(): AbilityEventEnum
    fun defineTargetPlayer(player: Player)
    fun resolve()
    fun cancel()
}

abstract class AbstractAbility(
    protected var targetPlayer: Player
): Ability{
    protected lateinit var event: AbilityEventEnum
    protected val onActionSubject = Subject<AbilitySignal>()
    override val playerObservable: Observable<AbilitySignal> = onActionSubject

    override fun fetchTargetPlayer(): Player{
        return targetPlayer
    }

    override fun fetchEvent(): AbilityEventEnum {
        return event
    }

    override fun defineTargetPlayer(player: Player){
        targetPlayer = player
    }

    override fun cancel(){
        event = AbilityEventEnum.CancelAbility
        onActionSubject.notify(AbilitySignal(this))
    }

    override fun resolve(){
        targetPlayer.receiveAbility(this)
    }
}

abstract class AttackAbility(targetPlayer: Player): AbstractAbility(targetPlayer){
    abstract val deathCause: DeathCause

    fun fetchDeathCause(): DeathCause{
        return deathCause
    }
}

abstract class VillagerAttackAbility(targetPlayer: Player): AttackAbility(targetPlayer)

abstract class WerewolfAttackAbility(targetPlayer: Player): AttackAbility(targetPlayer)

class Hang(targetPlayer: Player): VillagerAttackAbility(targetPlayer){
    override val deathCause: DeathCause = DeathCause.HANGED
    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.was_hanged)
    }

    override fun fetchPriority(): Int {
        return 0
    }
}

