package werewolf.model.entities.werewolves

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.Player
import werewolf.model.entities.WerewolfTeamPlayer
import werewolf.view.MyApp
import werewolf.view.R

class EvilJailer(
    playerName: String
): WerewolfTeamPlayer(playerName){

    override fun fetchImageSrc(): Int {
        return R.drawable.evil_jailer
    }

    override fun fetchRole(): Roles {
        return Roles.EvilJailer
    }

    override fun addUsedAbility() {
        usedAbilities.add(CancelPlayerAbility(targetPlayers[0]))
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