package werewolf.model.entities.werewolves

import werewolf.model.Roles
import werewolf.model.entities.AbilityState
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.DeathCause
import werewolf.model.entities.OneTurnCooldown
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.WerewolfTeamPlayer
import werewolf.view.MyApp
import werewolf.view.R

class Vampire(
    override val playerName: String
): WerewolfTeamPlayer(){
    override val role: Roles = Roles.Vampire
    override var abilityState: AbilityState = OneTurnCooldown()

    override fun fetchImageSrc(): Int {
        return R.drawable.vampire
    }

    override fun turnSetUp() {
        super.turnSetUp()
        signalEvent(PlayerEventEnum.SetWerewolfTargets)
    }

    override fun addUsedAbility() {
        abilityState = OneTurnCooldown()
        usedAbilities.add(VampireAttack(targetPlayer!!))
    }
}

class VampireAttack(targetPlayer: Player): AbstractAbility(targetPlayer) {
    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.vampire_attack)
    }

    override fun fetchPriority(): Int {
        return 4
    }

    override fun resolve() {
        targetPlayer.receiveDamage(DeathCause.MAULED)
    }

    override fun cancel(){

    }
}