package werewolf.model.entities.villagers

import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.Ability
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.Shot
import werewolf.view.R

class Vigilante(
    override val playerName: String
): AbstractPlayer(){
    override val role: String = "Vigilante"

    override fun resolveAbility(): Ability? {
        if (targetPlayer != null) {
            abilityState = NoUsesLeft()
            ability = Shot(targetPlayer!!)
        } else {
            ability = null
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