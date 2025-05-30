package werewolf.model.entities

import com.example.observer.Observable
import com.example.observer.Subject

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