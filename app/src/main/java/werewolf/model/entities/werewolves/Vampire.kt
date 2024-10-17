package werewolf.model.entities.werewolves

import werewolf.model.entities.Ability
import werewolf.model.entities.AbilityState
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.OneTurnCooldown
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.view.R

class Vampire(
    override val playerName: String
): AbstractPlayer(){
    override val role: String = "Vampire"
    override var abilityState: AbilityState = OneTurnCooldown()

    override fun fetchImageSrc(): Int {
        return R.drawable.vampire
    }

    override fun turnSetUp() {
        signalEvent(PlayerEventEnum.SetWerewolfTargets)
    }

    override fun useAbility(): Ability?{
        return if(targetPlayer!=null){
            abilityState.useAbility(this)
        } else{
            usedAbility = null
            usedAbility
        }
    }

    override fun resolveAbility(): Ability? {
        abilityState = OneTurnCooldown()
        usedAbility = VampireAttack(targetPlayer!!)
        return usedAbility
    }
}

class VampireAttack(targetPlayer: Player): AbstractAbility(targetPlayer) {
    override fun fetchAbilityName(): String {
        return "Vampire Attack"
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