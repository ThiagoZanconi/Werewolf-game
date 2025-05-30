package werewolf.model.entities.werewolves

import werewolf.model.Roles
import werewolf.model.entities.AbilityEventEnum
import werewolf.model.entities.AbilitySignal
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.Immune
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.WerewolfTeamPlayer
import werewolf.view.MyApp
import werewolf.view.R

class Necromancer(
    override val playerName: String
): WerewolfTeamPlayer(){
    override val role: Roles = Roles.Necromancer

    override fun addUsedAbility(){
        abilityState = NoUsesLeft()
        usedAbilities.add(ZombieSpell(targetPlayer!!))
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.necromancer
    }

    override fun turnSetUp() {
        super.turnSetUp()
        signalEvent(PlayerEventEnum.SetDeadTargets)
    }
}

class ZombieSpell(targetPlayer: Player): AbstractAbility(targetPlayer) {
    override fun resolve() {
        super.resolve()
        event = AbilityEventEnum.RevivePlayerZombie
        onActionSubject.notify(AbilitySignal(this))
        targetPlayer.defineDefenseState(Immune())
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.revive_spell)
    }

    override fun fetchPriority(): Int {
        return 1
    }
}