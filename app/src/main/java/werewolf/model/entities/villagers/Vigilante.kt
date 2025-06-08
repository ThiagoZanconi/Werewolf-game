package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.VillagerAttackAbility
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersEnum

class Vigilante(
    playerName: String
): AbstractPlayer(playerName) {

    override fun fetchImageSrc(): Int {
        return R.drawable.vigilante
    }

    override fun fetchRole(): Roles {
        return Roles.Vigilante
    }

    override fun addUsedAbility() {
        abilityState = NoUsesLeft()
        usedAbilities.add(Shot(targetPlayers[0]))
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.AlivePlayersTarget
    }
}

class Shot(targetPlayer: Player): VillagerAttackAbility(targetPlayer){
    override val deathCause = DeathCause.SHOT

    override fun resolve() {
        super.resolve()
        targetPlayer.receiveAttack(this)
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.shot)
    }

    override fun fetchPriority(): Int {
        return 4
    }
}