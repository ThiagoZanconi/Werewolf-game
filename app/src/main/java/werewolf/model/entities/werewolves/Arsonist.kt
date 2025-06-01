package werewolf.model.entities.werewolves

import androidx.fragment.app.Fragment
import com.example.observer.Subject
import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.DeathCause
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.PlayerEventEnum
import werewolf.model.entities.WerewolfTeamPlayer
import werewolf.view.GameUiEvent
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersEnum
import werewolf.view.TargetPlayersSignal
import werewolf.view.fragments.ArsonistFragment

class Arsonist(
    override val playerName: String
): WerewolfTeamPlayer(){
    override val role: Roles = Roles.Arsonist
    private val targetPlayers: MutableList<Player> = mutableListOf()
    private var ignite: Boolean = false

    fun setIgnite(ignite: Boolean){
        this.ignite = ignite
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.arsonist
    }

    override fun fetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.SetWerewolfTargets
    }

    override fun fetchView(onActionSubject: Subject<GameUiEvent>, targetPlayersOnActionSubject: Subject<TargetPlayersSignal>): Fragment {
        return ArsonistFragment(onActionSubject,this, targetPlayersOnActionSubject)
    }

    override fun addUsedAbility(){
        if(targetPlayer==null){
            abilityState = NoUsesLeft()
            usedAbilities.add(Ignite(targetPlayers))
        } else{
            usedAbilities.add(OilSpill(targetPlayer!!,targetPlayers))
        }
    }
}

class Ignite(private val targetPlayers: MutableList<Player>): AbstractAbility(Werewolf("Dummy Target")) {
    override fun resolve() {
        targetPlayers.forEach{
            it.receiveDamage(DeathCause.BURNT)
        }
        targetPlayers.clear()
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.ignite)
    }

    override fun fetchPriority(): Int {
        return 2
    }
}

class OilSpill(targetPlayer: Player,private val targetPlayers: MutableList<Player>): AbstractAbility(targetPlayer) {
    override fun resolve() {
        super.resolve()
        targetPlayers.add(targetPlayer)
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.oil_spill)
    }

    override fun fetchPriority(): Int {
        return 2
    }
}