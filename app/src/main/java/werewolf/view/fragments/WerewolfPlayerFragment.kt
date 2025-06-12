package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import com.example.observer.Subject
import werewolf.model.entities.Player
import werewolf.model.entities.WerewolfTeamPlayer
import werewolf.view.GameUiEvent
import werewolf.view.R
import werewolf.view.TargetPlayersEnum
import werewolf.view.TargetPlayersSignal

open class WerewolfPlayerFragment(
    onActionSubject: Subject<GameUiEvent>,
    player: WerewolfTeamPlayer,
    targetPlayersOnActionSubject: Subject<TargetPlayersSignal>
) : PlayerGridFragment(onActionSubject,player,targetPlayersOnActionSubject){
    private lateinit var teammatesGridLayout: GridLayout
    protected var teammates: List<Player> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_werewolf_teammates, container, false)
        initComponents(view)
        initListeners()

        return view
    }

    override fun initComponents(view: View) {
        super.initComponents(view)
        teammatesGridLayout = view.findViewById(R.id.teammatesGridLayout)
        initTeammatesGridLayout()
    }

    private fun initTeammatesGridLayout(){
        val teammatesPlayerSignal = TargetPlayersSignal(TargetPlayersEnum.WerewolfTeammates)
        targetPlayersOnActionSubject.notify(teammatesPlayerSignal)
        teammates = teammatesPlayerSignal.targetPlayers - listOf(player).toSet()
        teammates.forEach{
                player -> teammatesGridLayout.addView(createTextView(player.fetchPlayerName()),teammatesGridLayout.childCount)
        }
    }

    override fun setSelectedPlayer(playerName: String){
        super.setSelectedPlayer(playerName)
        if(selectedPlayer==null){
            selectedPlayer = teammates.find { it.fetchPlayerName() == playerName }
        }
    }

    override fun markNotSelected(playerName: String) {
        super.markNotSelected(playerName)
        for (i in 0 until teammatesGridLayout.childCount) {
            val child = teammatesGridLayout.getChildAt(i) as TextView
            if (child.text != playerName) {
                child.background = null
            }
        }
    }

}