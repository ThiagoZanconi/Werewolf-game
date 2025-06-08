package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbilityState
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.NoAbility
import werewolf.view.R

class Disguiser(
    playerName: String
): AbstractPlayer(playerName){
    override var abilityState: AbilityState = NoAbility()

    override fun fetchImageSrc(): Int {
        return R.drawable.disguiser
    }

    override fun fetchRole(): Roles {
        return Roles.Disguiser
    }
}