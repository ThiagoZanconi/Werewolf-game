package werewolf.model.entities.werewolves

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.DeathCause
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.WerewolfTeamPlayer
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersEnum

class Werewolf(
    override val playerName: String
) : WerewolfTeamPlayer(){
    override val role: Roles = Roles.Werewolf

    override fun fetchImageSrc(): Int {
        return R.drawable.werewolf
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.SetWerewolfTargets
    }

    override fun addUsedAbility(){
        usedAbilities.add(WerewolfAttack(targetPlayer!!))
    }

    override fun notifyKilledPlayer(deathCause: DeathCause) {
        this.deathCause = deathCause
        signalEvent(PlayerEventEnum.WerewolfKilled)
    }
}

class WerewolfAttack(targetPlayer: Player): AbstractAbility(targetPlayer) {
    override fun resolve(){
        super.resolve()
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