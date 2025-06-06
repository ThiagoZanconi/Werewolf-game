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

class DetectiveFragment(
    onActionSubject: Subject<GameUiEvent>,
    private val detective: Detective
) : AbilityPlayerFragment(onActionSubject,detective){
    private lateinit var stalkedTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detective, container, false)
        initComponents(view)
        initListeners()

        return view
    }

    override fun initComponents(view: View) {
        super.initComponents(view)
        stalkedTextView = view.findViewById(R.id.textViewStalked)
        stalkedTextView.text = detective.fetchInvestigatedPlayers()
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