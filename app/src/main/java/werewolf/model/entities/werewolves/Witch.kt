package werewolf.model.entities.werewolves

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.WerewolfTeamPlayer
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersEnum

class Witch(
    override val playerName: String
): WerewolfTeamPlayer(){
    override val role: Roles = Roles.Witch

    override fun fetchImageSrc(): Int {
        return R.drawable.witch
    }

    override fun fetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.SetWerewolfTargets
    }

    override fun addUsedAbility() {
        usedAbilities.add(CancelPlayerAbility(targetPlayer!!))
    }
}

class CancelPlayerAbility(targetPlayer: Player): AbstractAbility(targetPlayer) {
    override fun resolve() {
        super.resolve()
        targetPlayer.cancelAbility()
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.cancel_player_ability)
    }

    override fun fetchPriority(): Int {
        return 0
    }
}