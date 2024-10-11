package werewolf.model.entities.villagers

import werewolf.model.entities.Ability
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.ReviveSpell
import werewolf.view.R

class Priest(
    override val playerName: String
): AbstractPlayer(){
    override val role: String = "Priest"

    override fun resolveAbility(): Ability? {
        abilityState = NoUsesLeft()
        usedAbility = ReviveSpell(targetPlayer!!)
        return usedAbility
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.priest
    }

    override fun turnSetUp() {
        signalEvent(PlayerEventEnum.SetDeadTargets)
    }
}