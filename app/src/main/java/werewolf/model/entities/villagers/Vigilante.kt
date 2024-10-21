package werewolf.model.entities.villagers

import werewolf.model.entities.Ability
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.view.MyApp
import werewolf.view.R

class Vigilante(
    override val playerName: String
): AbstractPlayer(){
    override val role: String = MyApp.getAppContext().getString(R.string.vigilante)

    override fun resolveAbility(): Ability? {
        abilityState = NoUsesLeft()
        usedAbility = Shot(targetPlayer!!)
        return usedAbility
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.vigilante
    }

    override fun turnSetUp() {
        signalEvent(PlayerEventEnum.SetAlivePlayersTarget)
    }
}

class Shot(targetPlayer: Player): AbstractAbility(targetPlayer){

    override fun resolve() {
        targetPlayer.receiveDamage(DeathCause.SHOT)
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.shot)
    }

    override fun fetchPriority(): Int {
        return 2
    }
}