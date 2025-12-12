package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.Immune
import werewolf.model.entities.OneTurnCooldown
import werewolf.model.entities.Player
import werewolf.model.entities.TargetPlayersEnum
import werewolf.view.MyApp
import werewolf.view.R

class Lightbearer(
    playerName: String
): AbstractPlayer(playerName){

    override fun fetchImageSrc(): Int {
        return R.drawable.lightbearer
    }

    override fun fetchRole(): Roles {
        return Roles.Lightbearer
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.AlivePlayersTarget
    }

    override fun addUsedAbility() {
        abilityState = OneTurnCooldown()
        usedAbilities.add(Shield(targetPlayers[0]))
    }
}

class Shield(targetPlayer: Player): AbstractAbility(targetPlayer) {
    override fun resolve() {
        super.resolve()
        targetPlayer.defineDefenseState(Immune())
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.barrier)
    }

    override fun fetchPriority(): Int {
        return 2
    }
}