package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.Player
import werewolf.model.entities.Protected
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersEnum

class Protector(
    override val playerName: String
): AbstractPlayer(){
    override val role: Roles = Roles.Protector
    private var armored = true

    override fun addUsedAbility() {
        usedAbilities.add(Protection(targetPlayer!!,this))
    }

    override fun applyDamage(deathCause: DeathCause) {
        if(armored && deathCause!=DeathCause.HANGED){
            armored = false
        }
        else{
            notifyKilledPlayer(deathCause)
        }
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.protector
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.SetAlivePlayersTarget
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
        super.resolve()
        targetPlayer.defineDefenseState(Protected(protector))
    }
}