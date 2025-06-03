package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.Immune
import werewolf.model.entities.OneTurnCooldown
import werewolf.model.entities.Player
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersEnum

class Cleric(
    override val playerName: String
): AbstractPlayer(){
    override val role: Roles = Roles.Cleric

    override fun fetchImageSrc(): Int {
        return R.drawable.cleric
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.SetAlivePlayersTarget
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