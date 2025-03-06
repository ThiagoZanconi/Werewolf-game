package werewolf.model.entities.neutrals

import werewolf.model.Roles
import werewolf.model.entities.Ability
import werewolf.model.entities.AbilityState
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.NoAbility
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.PlayerSignal
import werewolf.view.R

class Jester(
    override val playerName: String
): AbstractPlayer(){
    override val role: Roles = Roles.Jester
    override var abilityState: AbilityState = NoAbility()

    override fun resolveAbility(): Ability? {
        return null
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
        onActionSubject.notify(PlayerSignal(this))
    }
}