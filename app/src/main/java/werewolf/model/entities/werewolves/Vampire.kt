package werewolf.model.entities.werewolves

import werewolf.model.Roles
import werewolf.model.entities.Ability
import werewolf.model.entities.AbilityState
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.OneTurnCooldown
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.view.MyApp
import werewolf.view.R

class Vampire(
    override val playerName: String
): AbstractPlayer(){
    override val role: Roles = Roles.Vampire
    override var abilityState: AbilityState = OneTurnCooldown()

    override fun fetchImageSrc(): Int {
        return R.drawable.vampire
    }

    override fun turnSetUp() {
        signalEvent(PlayerEventEnum.SetWerewolfTargets)
    }

    override fun useAbility(): Ability?{
        return abilityState.useAbility(this)
    }

    override fun resolveAbility(): Ability? {
        return if(targetPlayer!=null){
            abilityState = OneTurnCooldown()
            usedAbility = VampireAttack(targetPlayer!!)
            usedAbility
        } else{
            usedAbility = null
            usedAbility
        }
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