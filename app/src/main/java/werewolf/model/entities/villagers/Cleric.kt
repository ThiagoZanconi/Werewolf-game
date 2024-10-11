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
    private val COOLDOW = 2

    override fun fetchImageSrc(): Int {
        return R.drawable.cleric
    }

    override fun turnSetUp() {
        signalEvent(PlayerEventEnum.SetAlivePlayersTarget)
    }

    override fun useAbility(): Ability?{
        return if(targetPlayer!=null){
            abilityState.useAbility(this)
        } else
            null
    }

    override fun resolveAbility(): Ability? {
        abilityState = Cooldown()
        usedAbility = Shield(targetPlayer!!)
        return usedAbility
    }

    override fun cooldownTimer() {
        abilityState = Neutral()
    }
}