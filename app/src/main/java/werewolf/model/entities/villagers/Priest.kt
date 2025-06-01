package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbilityEventEnum
import werewolf.model.entities.AbilitySignal
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.Immune
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersEnum

class Priest(
    override val playerName: String
): AbstractPlayer(){
    override val role: Roles = Roles.Priest

    override fun addUsedAbility() {
        abilityState = NoUsesLeft()
        usedAbilities.add(ReviveSpell(targetPlayer!!))
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.priest
    }

    override fun fetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.SetDeadTargets
    }
}

class ReviveSpell(targetPlayer: Player): AbstractAbility(targetPlayer) {
    override fun resolve() {
        super.resolve()
        event = AbilityEventEnum.RevivePlayer
        onActionSubject.notify(AbilitySignal(this))
        targetPlayer.defineDefenseState(Immune())
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.revive_spell)
    }

    override fun fetchPriority(): Int {
        return 2
    }
}