package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.observer.Subject
import werewolf.model.entities.villagers.Veteran
import werewolf.view.GameUiEvent
import werewolf.view.MyApp
import werewolf.view.R

class VeteranFragment(
    onActionSubject: Subject<GameUiEvent>,
    veteran: Veteran
) : AbilityPlayerFragment(onActionSubject,veteran){

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
        abilityButton.text = MyApp.getAppContext().getString(R.string.alert)
    }

    override fun initAbilitySelectedButton(view: View){
        super.initAbilitySelectedButton(view)
        abilitySelectedButton.text = MyApp.getAppContext().getString(R.string.alert)
    }

}