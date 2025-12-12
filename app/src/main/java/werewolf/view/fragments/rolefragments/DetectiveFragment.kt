package werewolf.view.fragments.rolefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.observer.Subject
import org.json.JSONObject
import werewolf.view.GameUiEventSignal
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.fragments.AbilityPlayerFragment
import werewolf.view.fragments.PlayerGridFragment

class DetectiveFragment(
    onActionSubject: Subject<GameUiEventSignal>,
    detective: JSONObject
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
    onActionSubject: Subject<GameUiEventSignal>,
    jsonObject: JSONObject
): PlayerGridFragment(onActionSubject,jsonObject){

    override fun initComponents(view: View) {
        super.initComponents(view)
        descriptionLabel.text = requireContext().getString(R.string.candidates)
    }

    override fun initGridLayout(){
        val jsonArray = jsonObject.getJSONArray("InvestigatedPlayers")
        val investigatedPlayers = List(jsonArray.length()) { i ->
            jsonArray.getString(i)
        }
        investigatedPlayers.forEach {
            gridLayout.addView(createTextView(it),gridLayout.childCount)
        }
    }

    override fun onPlayerClick(textView: TextView, playerName: String) {

    }
}