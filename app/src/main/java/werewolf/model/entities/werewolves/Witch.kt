package werewolf.model.entities.werewolves

import werewolf.model.entities.Ability
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.view.MyApp
import werewolf.view.R

class Witch(
    override val playerName: String
): AbstractPlayer(){
    override val role: String = MyApp.getAppContext().getString(R.string.witch)

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
        return MyApp.getAppContext().getString(R.string.cancel_player_ability)
    }

    override fun fetchPriority(): Int {
        return 1
    }
}