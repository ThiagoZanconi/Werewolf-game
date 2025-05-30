package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.Ability
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.view.MyApp
import werewolf.view.R

class Elusive(
    override val playerName: String
): AbstractPlayer(){
    override val role: Roles = Roles.Elusive

    override fun resolveAbility(): Ability? {
        usedAbility = Hide(targetPlayer!!)
        return usedAbility
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.elusive
    }

    override fun turnSetUp() {
        super.turnSetUp()
        signalEvent(PlayerEventEnum.SetOtherAlivePlayersTarget)
    }
}

class Hide(targetPlayer: Player): AbstractAbility(targetPlayer){

    override fun resolve() {
        //targetPlayer.receiveAbility(Hidden)
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.shot)
    }

    override fun fetchPriority(): Int {
        return 1
    }
}