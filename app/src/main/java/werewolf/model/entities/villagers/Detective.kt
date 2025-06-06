package werewolf.model.entities.villagers

import androidx.fragment.app.Fragment
import com.example.observer.Subject
import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.werewolves.Werewolf
import werewolf.view.GameUiEvent
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersEnum
import werewolf.view.TargetPlayersSignal
import werewolf.view.fragments.DetectiveFragment
import werewolf.view.fragments.DetectiveGridFragment

class Detective(
    override val playerName: String,
    private val werewolves: MutableList<Player>,
    private val villagers: MutableList<Player>,
): AbstractPlayer(){
    override val role: Roles = Roles.Detective
    private var investigatedPlayers: List<Player>? = null

    override fun fetchImageSrc(): Int {
        return R.drawable.detective
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.SetNoTargetPlayers
    }

    override fun addUsedAbility() {
        investigatedPlayers = listOf()
        abilityState = NoUsesLeft()
        val werewolf = werewolves.random()
        usedAbilities.add(Investigate(this, werewolf,(werewolves + villagers - listOf(this).toSet() - listOf(werewolf).toSet()).shuffled()))
    }

    override fun fetchView(onActionSubject: Subject<GameUiEvent>, targetPlayersOnActionSubject: Subject<TargetPlayersSignal>): Fragment {
        return if(investigatedPlayers==null){
            DetectiveFragment(onActionSubject,this)
        } else{
            DetectiveGridFragment(onActionSubject,this,targetPlayersOnActionSubject)
        }
    }

    fun fetchInvestigatedPlayers(): List<Player>{
        return investigatedPlayers!!
    }

    fun defineInvestigatedPlayers(investigatedPlayers: List<Player>){
        this.investigatedPlayers = investigatedPlayers
    }

}

class Investigate(private val detective: Detective, private val werewolf: Player, private val playerList: List<Player>): AbstractAbility(Werewolf("Dummy Target")) {
    override fun resolve() {
        super.resolve()
        val investigatedPlayersList: MutableList<Player> =  mutableListOf(werewolf)
        investigatedPlayersList.add(playerList[0])
        investigatedPlayersList.add(playerList[1])
        detective.defineInvestigatedPlayers(investigatedPlayersList.shuffled())
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.investigate)
    }

    override fun fetchPriority(): Int {
        return 3
    }
}