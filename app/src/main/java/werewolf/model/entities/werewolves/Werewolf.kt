package werewolf.model.entities.werewolves

import werewolf.model.entities.Ability
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.WerewolfAttack
import werewolf.view.R

class Werewolf(
    override val playerName: String
) : AbstractPlayer(){
    override val role: String = "Werewolf"

    override fun fetchImageSrc(): Int {
        return R.drawable.werewolf
    }

    override fun turnSetUp() {
        signalEvent(PlayerEventEnum.SetWerewolfTargets)
    }

    override fun resolveAbility(): Ability? {
        usedAbility = WerewolfAttack(targetPlayer!!)
        return usedAbility
    }

    override fun notifyKilledPlayer(deathCause: DeathCause) {
        this.deathCause = deathCause
        signalEvent(PlayerEventEnum.WerewolfKilled)
    }
}