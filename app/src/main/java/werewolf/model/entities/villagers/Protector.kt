package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.Player
import werewolf.model.entities.Protected
import werewolf.model.entities.TargetPlayersEnum
import werewolf.view.MyApp
import werewolf.view.R

class Protector(
    playerName: String
): AbstractPlayer(playerName){
    private var armored = true

    override fun fetchImageSrc(): Int {
        return R.drawable.protector
    }

    override fun fetchRole(): Roles {
        return Roles.Protector
    }

    override fun addUsedAbility() {
        usedAbilities.add(Protection(targetPlayers[0],this))
    }

    override fun applyDamage(deathCause: DeathCause) {
        if(armored && deathCause!=DeathCause.HANGED){
            armored = false
        }
        else{
            notifyKilledPlayer(deathCause)
        }
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.OtherPlayersTarget
    }
}

class Protection(targetPlayer: Player, private val protector: Protector): AbstractAbility(targetPlayer){
    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.protection)
    }

    override fun fetchPriority(): Int {
        return 2
    }

    override fun resolve() {
        super.resolve()
        targetPlayer.defineDefenseState(Protected(protector))
    }
}