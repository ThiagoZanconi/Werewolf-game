package werewolf.model.entities.villagers

import werewolf.model.entities.Ability
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.Immune
import werewolf.model.entities.OneTurnCooldown
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
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

class Shield(targetPlayer: Player): AbstractAbility(targetPlayer) {
    override fun resolve() {
        targetPlayer.defineDefenseState(Immune())
    }

    override fun fetchAbilityName(): String {
        return "Shield"
    }

    override fun fetchPriority(): Int {
        return 2
    }
}