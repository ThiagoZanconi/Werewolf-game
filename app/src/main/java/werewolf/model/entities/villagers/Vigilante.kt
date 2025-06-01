package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersEnum

class Vigilante(
    override val playerName: String
): AbstractPlayer(){
    override val role: Roles = Roles.Vigilante

    override fun addUsedAbility() {
        abilityState = NoUsesLeft()
        usedAbilities.add(Shot(targetPlayer!!))
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.vigilante
    }

    override fun fetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.SetAlivePlayersTarget
    }
}

class Shot(targetPlayer: Player): AbstractAbility(targetPlayer){

    override fun resolve() {
        super.resolve()
        targetPlayer.receiveDamage(DeathCause.SHOT)
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.shot)
    }

    override fun fetchPriority(): Int {
        return 4
    }
}