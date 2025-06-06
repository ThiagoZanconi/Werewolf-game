package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.observer.Subject
import werewolf.model.entities.villagers.Detective
import werewolf.view.GameUiEvent
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersSignal

class DetectiveFragment(
    onActionSubject: Subject<GameUiEvent>,
    detective: Detective
) : AbilityPlayerFragment(onActionSubject,detective){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player_ability, container, false)
        initComponents(view)
        initListeners()

        return view
    }

    override fun initAbilityButton(view: View){
        super.initAbilityButton(view)
        abilityButton.text = MyApp.getAppContext().getString(R.string.investigate)
    }

    override fun initAbilitySelectedButton(view: View){
        super.initAbilitySelectedButton(view)
        abilitySelectedButton.text = MyApp.getAppContext().getString(R.string.investigate)
    }

}

class DetectiveGridFragment(
    onActionSubject: Subject<GameUiEvent>,
    private val detective: Detective,
    targetPlayersOnActionSubject: Subject<TargetPlayersSignal>
): PlayerGridFragment(onActionSubject,detective,targetPlayersOnActionSubject){
    override fun initGridLayout(){
        detective.fetchInvestigatedPlayers().forEach {
            gridLayout.addView(createTextView(it.fetchPlayerName()),gridLayout.childCount)
        }
    }

    override fun onPlayerClick(textView: TextView, playerName: String) {

    }
}