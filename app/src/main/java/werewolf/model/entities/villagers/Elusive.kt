package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.werewolves.Werewolf
import werewolf.view.MyApp
import werewolf.view.R

class Elusive(
    override val playerName: String
): AbstractPlayer(){
    override val role: Roles = Roles.Elusive

    override fun addUsedAbility() {
        usedAbilities.add(Hidden(this,targetPlayer!!))
        usedAbilities.add(Hide(this))
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.elusive
    }

    override fun turnSetUp() {
        super.turnSetUp()
        signalEvent(PlayerEventEnum.SetOtherAlivePlayersTarget)
    }
}

class Hidden(private val hiddenPlayer: Player,targetPlayer: Player): AbstractAbility(targetPlayer){

    override fun resolve() {
        if(targetPlayer is Werewolf){
            hiddenPlayer.receiveDamage(deathCause = DeathCause.MAULED)
        }
        targetPlayer.fetchAbilitiesUsedOnMe().forEach{
            it.defineTargetPlayer(hiddenPlayer)
            it.resolve()
        }
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.shot)
    }

    override fun fetchPriority(): Int {
        return 15
    }
}

class Hide(private val hiddenPlayer: Player): AbstractAbility(Werewolf("Dummy target")){

    override fun resolve() {
        //hiddenPlayer.set
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.shot)
    }

    override fun fetchPriority(): Int {
        return 1
    }
}