package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.observer.Subject
import werewolf.model.entities.villagers.Detective
import werewolf.view.GameUiEvent
import werewolf.view.R
import werewolf.view.TargetPlayersSignal

class DetectiveFragment(
    onActionSubject: Subject<GameUiEvent>,
    private val detective: Detective,
    targetPlayersOnActionSubject: Subject<TargetPlayersSignal>
) : PlayerGridFragment(onActionSubject,detective,targetPlayersOnActionSubject){
    private lateinit var stalkedTextView: TextView
    private lateinit var investigateButton: TextView
    private lateinit var investigateSelectedButton: TextView
    private var investigate: Boolean = false

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

    override fun confirmAction(){
        if(investigate){
            player.notifyAbilityUsed(null)
        }
        onActionSubject.notify(GameUiEvent.ConfirmAction)
    }

    override fun initComponents(view: View) {
        super.initComponents(view)
        initInvestigateButton(view)
        initInvestigateSelectedButton(view)
        stalkedTextView = view.findViewById(R.id.textViewStalked)
        stalkedTextView.text = detective.fetchInvestigatedPlayers()
    }

    private fun initInvestigateButton(view: View){
        investigateButton = view.findViewById(R.id.igniteTextView)
        investigateButton.setOnClickListener{ investigateButtonOnClickListener() }
    }

    private fun initInvestigateSelectedButton(view: View){
        investigateSelectedButton = view.findViewById(R.id.igniteSelectedTextView)
        investigateSelectedButton.setOnClickListener{ investigateSelectedButtonOnClickListener() }
    }

    private fun investigateButtonOnClickListener(){
        investigate = true
        investigateButton.visibility=View.GONE
        investigateSelectedButton.visibility=View.VISIBLE
    }

    private fun investigateSelectedButtonOnClickListener(){
        investigate = false
        investigateSelectedButton.visibility=View.GONE
        investigateButton.visibility=View.VISIBLE
    }

}