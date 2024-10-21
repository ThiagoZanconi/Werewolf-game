package werewolf.model.entities.villagers

import werewolf.model.entities.Ability
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.Protected
import werewolf.view.MyApp
import werewolf.view.R

class Protector(
    override val playerName: String
): AbstractPlayer(){
    override val role: String = MyApp.getAppContext().getString(R.string.protector)
    private var armored = true

    override fun resolveAbility(): Ability? {
        usedAbility = Protection(targetPlayer!!,this)
        return usedAbility
    }

    override fun applyDamage(deathCause: DeathCause) {
        if(armored){
            armored = false
        }
        else{
            notifyKilledPlayer(deathCause)
        }
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.protector
    }

    override fun turnSetUp() {
        signalEvent(PlayerEventEnum.SetOtherAlivePlayersTarget)
    }
}

class Protection(targetPlayer: Player, private val protector: Protector): AbstractAbility(targetPlayer){
    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.protection)
    }

    override fun fetchPriority(): Int {
        return 3
    }

    override fun resolve() {
        targetPlayer.defineDefenseState(Protected(protector))
    }
}