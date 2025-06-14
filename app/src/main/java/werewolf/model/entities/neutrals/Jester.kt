package werewolf.model.entities.neutrals

import werewolf.model.Roles
import werewolf.model.entities.AbilityState
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.NoAbility
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.PlayerSignal
import werewolf.view.R

class Jester(
    playerName: String
): AbstractPlayer(playerName){
    override var abilityState: AbilityState = NoAbility()

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

    override fun fetchRole(): Roles {
        return Roles.Jester
    }
}