package werewolf.model.entities.werewolves

import werewolf.model.entities.Ability
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.view.MyApp
import werewolf.view.R

class Werewolf(
    override val playerName: String
) : AbstractPlayer(){
    override val role: String = MyApp.getAppContext().getString(R.string.werewolf)

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

class WerewolfAttack(targetPlayer: Player): AbstractAbility(targetPlayer) {
    override fun resolve(){
        targetPlayer.receiveDamage(DeathCause.MAULED)
    }

    override fun fetchPriority(): Int {
        return 4
    }

    override fun cancel(){

    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.werewolf_attack)
    }
}