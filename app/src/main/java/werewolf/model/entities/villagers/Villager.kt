package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.Ability
import werewolf.model.entities.AbilityState
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.NoAbility
import werewolf.view.MyApp
import werewolf.view.R

class Villager(
    override val playerName: String
): AbstractPlayer() {
    override val role: Roles = Roles.Villager
    override var abilityState: AbilityState = NoAbility()

    override fun fetchImageSrc(): Int {
        return R.drawable.villager
    }

    override fun resolveAbility(): Ability? {
        return usedAbility
    }
}