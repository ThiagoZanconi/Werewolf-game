package werewolf.model.entities.villagers

import werewolf.model.entities.Ability
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.OneTurnCooldown
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.Shield
import werewolf.view.R

class Cleric(
    override val playerName: String
): AbstractPlayer(){
    override val role: String = "Cleric"

    override fun fetchImageSrc(): Int {
        return R.drawable.cleric
    }

    override fun turnSetUp() {
        signalEvent(PlayerEventEnum.SetAlivePlayersTarget)
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
        usedAbility = Shield(targetPlayer!!)
        return usedAbility
    }
}