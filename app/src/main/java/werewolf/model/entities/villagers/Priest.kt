package werewolf.model.entities.villagers

import werewolf.model.entities.Ability
import werewolf.model.entities.AbilityEventEnum
import werewolf.model.entities.AbilitySignal
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.Immune
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
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

class ReviveSpell(targetPlayer: Player): AbstractAbility(targetPlayer) {
    override fun resolve() {
        event = AbilityEventEnum.RevivePlayer
        onActionSubject.notify(AbilitySignal(this))
        targetPlayer.defineDefenseState(Immune())
    }

    override fun fetchAbilityName(): String {
        return "Revive Spell"
    }

    override fun fetchPriority(): Int {
        return 2
    }
}