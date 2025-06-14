package werewolf.model.entities.werewolves

import werewolf.model.Roles
import werewolf.model.entities.AbilityState
import werewolf.model.entities.DeathCause
import werewolf.model.entities.OneTurnCooldown
import werewolf.model.entities.Player
import werewolf.model.entities.WerewolfAttackAbility
import werewolf.model.entities.WerewolfTeamPlayer
import werewolf.view.MyApp
import werewolf.view.R

class Vampire(
    playerName: String
): WerewolfTeamPlayer(playerName){
    override var abilityState: AbilityState = OneTurnCooldown()

    override fun fetchImageSrc(): Int {
        return R.drawable.vampire
    }

    override fun fetchRole(): Roles {
        return Roles.Vampire
    }

    override fun addUsedAbility() {
        abilityState = OneTurnCooldown()
        usedAbilities.add(VampireAttack(targetPlayers[0]))
    }
}

class VampireAttack(targetPlayer: Player): WerewolfAttackAbility(targetPlayer) {
    override val deathCause: DeathCause = DeathCause.MAULED

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.vampire_attack)
    }

    override fun fetchPriority(): Int {
        return 4
    }

    override fun resolve() {
        super.resolve()
        targetPlayer.receiveAttack(this)
    }

    override fun cancel(){

    }
}