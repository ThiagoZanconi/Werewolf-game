package werewolf.model.entities.werewolves

import werewolf.model.entities.Ability
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.Player
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
        usedAbility = CancelPlayerAbility(targetPlayer!!)
        return usedAbility
    }
}

class CancelPlayerAbility(targetPlayer: Player): AbstractAbility(targetPlayer) {
    override fun resolve() {
        targetPlayer.cancelAbility()
    }

    override fun fetchAbilityName(): String {
        return "Null Player Ability"
    }

    override fun fetchPriority(): Int {
        return 1
    }
}