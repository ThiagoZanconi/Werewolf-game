package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbilityState
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.NoAbility
import werewolf.view.R

class Disguiser(
    override val playerName: String
): AbstractPlayer() {
    override val role: Roles = Roles.Disguiser
    override var abilityState: AbilityState = NoAbility()

    override fun fetchImageSrc(): Int {
        return R.drawable.disguiser
    }
}