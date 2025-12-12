package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.werewolves.Werewolf
import werewolf.view.FragmentEnum
import werewolf.view.MyApp
import werewolf.view.R

class Veteran(
    playerName: String
): AbstractPlayer(playerName){

    override fun fetchImageSrc(): Int {
        return R.drawable.veteran
    }

    override fun fetchRole(): Roles {
        return Roles.Veteran
    }

    override fun addUsedAbility() {
        abilityState = NoUsesLeft()
        usedAbilities.add(Alert(visitors))
    }

    override fun resolveFetchView(): FragmentEnum {
        return FragmentEnum.VeteranFragment
    }
}

class Alert(private val targetPlayers: MutableList<Player>): AbstractAbility(Werewolf("Dummy Target")){

    override fun resolve() {
        targetPlayers.forEach{
            it.receiveAttack(Shot(it))
        }
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.alert)
    }

    override fun fetchPriority(): Int {
        return 3
    }
}