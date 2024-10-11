package werewolf.model.entities.neutrals

import werewolf.model.entities.AbilityState
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.EndOfRoundAbility
import werewolf.model.entities.EventSignal
import werewolf.model.entities.NoAbility
import werewolf.model.entities.NullAbility
import werewolf.model.entities.PlayerEventEnum
import werewolf.view.R

class Jester(
    override val playerName: String
): AbstractPlayer(){
    override val role: String = "Jester"
    override var abilityState: AbilityState = NoAbility()

    override fun resolveAbility(): EndOfRoundAbility {
        return NullAbility()
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.jester
    }

    override fun notifyKilledPlayer(deathCause: DeathCause) {
        this.deathCause = deathCause
        event = if(deathCause == DeathCause.HANGED){
            PlayerEventEnum.JesterWin
        } else{
            PlayerEventEnum.KilledPlayer
        }
        onActionSubject.notify(EventSignal(this))
    }
}