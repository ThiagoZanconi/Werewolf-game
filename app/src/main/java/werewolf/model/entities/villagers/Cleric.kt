package werewolf.model.entities.villagers

import werewolf.model.entities.Ability
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.Cooldown
import werewolf.model.entities.Neutral
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.Shield
import werewolf.view.R

class Cleric(
    override val playerName: String
): AbstractPlayer(){
    override val role: String = "Cleric"
    private val COOLDOWN = 1
    private var remainingCooldown = 0

    override fun fetchImageSrc(): Int {
        return R.drawable.cleric
    }

    override fun turnSetUp() {
        signalEvent(PlayerEventEnum.SetAlivePlayersTarget)
    }

    override fun useAbility(): Ability?{
        cooldownTimer()
        return if(targetPlayer!=null){
            abilityState.useAbility(this)
        } else{
            usedAbility = null
            usedAbility
        }
    }

    override fun resolveAbility(): Ability? {
        remainingCooldown = COOLDOWN
        abilityState = Cooldown()
        usedAbility = Shield(targetPlayer!!)
        return usedAbility
    }

    private fun cooldownTimer(){
        when (remainingCooldown) {
            0 -> abilityState = Neutral()
            else -> remainingCooldown--
        }
    }
}