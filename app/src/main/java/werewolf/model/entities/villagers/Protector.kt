package werewolf.model.entities.villagers

import werewolf.model.entities.Ability
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.Protection
import werewolf.view.R

class Protector(
    override val playerName: String
): AbstractPlayer(){
    override val role: String = "Protector"
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