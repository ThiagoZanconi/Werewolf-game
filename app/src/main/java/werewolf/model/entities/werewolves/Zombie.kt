package werewolf.model.entities.werewolves

import werewolf.model.Roles
import werewolf.model.entities.AbilityState
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.NoAbility
import werewolf.model.entities.WerewolfAttackAbility
import werewolf.view.R

class Zombie(
    playerName: String
): AbstractPlayer(playerName) {
    override var abilityState: AbilityState = NoAbility()

    override fun fetchImageSrc(): Int {
        return R.drawable.zombie
    }

    override fun fetchRole(): Roles {
        return Roles.Zombie
    }

    override fun receiveAttack(werewolfAttackAbility: WerewolfAttackAbility) {

    }
}