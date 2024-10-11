package werewolf.model.entities.werewolves

import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.EndOfRoundAbility
import werewolf.model.entities.NullAbility
import werewolf.model.entities.NullPlayerAbility
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

    override fun resolveAbility(): EndOfRoundAbility {
        return targetPlayer?.let { NullPlayerAbility(it) } ?: NullAbility()
    }
}