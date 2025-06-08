package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.Player
import werewolf.model.entities.werewolves.Werewolf
import werewolf.model.entities.werewolves.WerewolfAttack
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersEnum

class Elusive(
    playerName: String
): AbstractPlayer(playerName){

    override fun fetchImageSrc(): Int {
        return R.drawable.elusive
    }

    override fun fetchRole(): Roles {
        return Roles.Elusive
    }

    override fun addUsedAbility() {
        usedAbilities.add(Hidden(this,targetPlayers[0]))
        usedAbilities.add(Hide(this,visitors))
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.OtherPlayersTarget
    }
}

class Hidden(private val hiddenPlayer: Player,targetPlayer: Player): AbstractAbility(targetPlayer){

    override fun resolve() {
        if(targetPlayer is Werewolf){
            hiddenPlayer.receiveAttack(WerewolfAttack(hiddenPlayer))
        }
        targetPlayer.fetchAbilitiesUsedOnMe().forEach{
            it.defineTargetPlayer(hiddenPlayer)
            it.resolve()
        }
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.hidden)
    }

    override fun fetchPriority(): Int {
        return 15
    }
}

class Hide(private val hiddenPlayer: Elusive, private val visitors: List<Player>): AbstractAbility(Werewolf("Dummy target")){

    override fun resolve() {
        visitors.forEach { visitor ->
            visitor.fetchUsedAbilities().forEach { ability ->
                if(ability.fetchTargetPlayer() === hiddenPlayer){
                    visitor.cancelAbility()
                }
            }
        }
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.hide)
    }

    override fun fetchPriority(): Int {
        return 1
    }
}