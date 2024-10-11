package werewolf.model.entities.werewolves

import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.CancelPlayerAbility
import werewolf.model.entities.Ability
import werewolf.model.entities.PlayerEventEnum
import werewolf.view.R

class Witch(
    override val playerName: String
): AbstractPlayer(){
    override val role: String = "Witch"

    override fun fetchImageSrc(): Int {
        return R.drawable.witch
    }

    override fun turnSetUp() {
        signalEvent(PlayerEventEnum.SetWerewolfTargets)
    }

    override fun resolveAbility(): Ability? {
        return targetPlayer?.let { CancelPlayerAbility(it) }
    }
}