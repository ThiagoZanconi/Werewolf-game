package werewolf.model.entities.werewolves

import androidx.fragment.app.Fragment
import com.example.observer.Subject
import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.WerewolfTeamPlayer
import werewolf.model.entities.villagers.Bomb
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
    private val oiledPlayers: MutableList<Player> = mutableListOf(this)
    private var ignite: Boolean = false

    fun setIgnite(ignite: Boolean){
        this.ignite = ignite
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.arsonist
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.SetWerewolfTargets
    }

    override fun fetchView(onActionSubject: Subject<GameUiEvent>, targetPlayersOnActionSubject: Subject<TargetPlayersSignal>): Fragment {
        return ArsonistFragment(onActionSubject,this, targetPlayersOnActionSubject)
    }

    override fun notifyAbilityUsed(targetPlayer: Player?){
        targetPlayers.forEach {
            it.visitedBy(this)
        }
        abilityState.useAbility(this)
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
        targetPlayers.clear()
    }

    fun setOilTargets(oilTargets: List<Player>){
        this.targetPlayers = oilTargets.toMutableList()
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
        return 2
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
        return 2
    }
}