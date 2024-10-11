package werewolf.model.entities.villagers

import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.EndOfRoundAbility
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.NullAbility
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.Shot
import werewolf.view.R

class Vigilante(
    override val playerName: String
): AbstractPlayer(){
    override val role: String = "Vigilante"

    override fun resolveAbility(): EndOfRoundAbility {
        if (targetPlayer != null) {
            abilityState = NoUsesLeft()
            ability = Shot(targetPlayer!!)
        } else {
            ability = NullAbility()
        }
        return ability
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.vigilante
    }

    override fun turnSetUp() {
        signalEvent(PlayerEventEnum.SetAlivePlayersTarget)
    }
}