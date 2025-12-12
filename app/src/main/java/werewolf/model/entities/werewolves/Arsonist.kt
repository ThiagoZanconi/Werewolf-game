package werewolf.model.entities.werewolves

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.WerewolfTeamPlayer
import werewolf.model.entities.villagers.Bomb
import werewolf.view.FragmentEnum
import werewolf.view.MyApp
import werewolf.view.R

class Arsonist(
    playerName: String
): WerewolfTeamPlayer(playerName){
    private val oiledPlayers: MutableList<Player> = mutableListOf(this)

    override fun fetchImageSrc(): Int {
        return R.drawable.arsonist
    }

    override fun fetchRole(): Roles {
        return Roles.Arsonist
    }

    override fun fetchFragment(): FragmentEnum {
        return FragmentEnum.ArsonistFragment
    }

    override fun addUsedAbility(){
        if(targetPlayers.isEmpty()){
            abilityState = NoUsesLeft()
            usedAbilities.add(Ignite(oiledPlayers))
        }
        else{
            targetPlayers.forEach {
                usedAbilities.add(OilSpill(it,oiledPlayers))
            }
        }
    }
}

class Ignite(private val targetPlayers: MutableList<Player>): AbstractAbility(Werewolf("Dummy Target")) {
    override fun resolve() {
        targetPlayers.forEach{
            it.receiveAttack(Bomb(it))
        }
        targetPlayers.clear()
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.ignite)
    }

    override fun fetchPriority(): Int {
        return 3
    }
}

class OilSpill(targetPlayer: Player,private val oiledPlayers: MutableList<Player>): AbstractAbility(targetPlayer) {
    override fun resolve() {
        super.resolve()
        oiledPlayers.add(targetPlayer)
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.oil_spill)
    }

    override fun fetchPriority(): Int {
        return 3
    }
}