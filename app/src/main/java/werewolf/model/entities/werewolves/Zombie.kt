package werewolf.model.entities.werewolves

import werewolf.model.Roles
import werewolf.model.entities.AbilityState
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.NoAbility
import werewolf.view.R

class Zombie(
    override val playerName: String
): AbstractPlayer() {
    override val role: Roles = Roles.Zombie
    override var abilityState: AbilityState = NoAbility()

    override fun fetchImageSrc(): Int {
        return R.drawable.zombie
    }
}