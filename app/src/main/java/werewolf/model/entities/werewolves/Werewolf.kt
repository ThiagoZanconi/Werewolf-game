package werewolf.model.entities.werewolves

import werewolf.model.Roles
import werewolf.model.entities.DeathCause
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.WerewolfAttackAbility
import werewolf.model.entities.WerewolfTeamPlayer
import werewolf.view.MyApp
import werewolf.view.R

class Werewolf(
    playerName: String
): WerewolfTeamPlayer(playerName){

    override fun fetchImageSrc(): Int {
        return R.drawable.werewolf
    }

    override fun fetchRole(): Roles {
        return Roles.Werewolf
    }

    override fun addUsedAbility(){
        usedAbilities.add(WerewolfAttack(targetPlayers[0]))
    }

    override fun notifyKilledPlayer(deathCause: DeathCause) {
        this.deathCause = deathCause
        signalEvent(PlayerEventEnum.WerewolfKilled)
    }
}

class WerewolfAttack(targetPlayer: Player): WerewolfAttackAbility(targetPlayer) {
    override val deathCause: DeathCause = DeathCause.MAULED

    override fun resolve(){
        super.resolve()
        targetPlayer.receiveAttack(this)
    }

    override fun fetchPriority(): Int {
        return 4
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.werewolf_attack)
    }
}