package werewolf.model.entities.villagers

import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.Cooldown
import werewolf.model.entities.EndOfRoundAbility
import werewolf.model.entities.Neutral
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

    override fun resolveAbility(): EndOfRoundAbility? {
        if (targetPlayer != null) {
            abilityState = Cooldown()
            ability = Shield(targetPlayer!!)
        } else {
            ability = null
        }
        return ability
    }

    override fun cooldownTimer() {
        abilityState = Neutral()
    }
}