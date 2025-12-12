package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.observer.Subject
import org.json.JSONObject
import werewolf.model.entities.Player
import werewolf.view.GameUiEvent
import werewolf.view.GameUiEventSignal
import werewolf.view.R

class FinishedRoundFragment(
    onActionSubject: Subject<GameUiEventSignal>,
    private val text: String,
    private val alivePlayers: List<Player>
): GridFragment(onActionSubject, JSONObject()){
    private lateinit var summaryTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_finishedround, container, false)
        initComponents(view)
        initListeners()
        return view
    }

    override fun initComponents(view: View) {
        confirmButton = view.findViewById(R.id.confirmButton)
        titleLabel = view.findViewById(R.id.titleLabel)
        gridLayout = view.findViewById(R.id.gridLayout)
        initGridLayout()
        titleLabel.text = requireContext().getString(R.string.round_finished)
        summaryTextView = view.findViewById(R.id.waitTextView)
        summaryTextView.text = text
    }

    override fun initListeners() {
        confirmButton.setOnClickListener { showConfirmActionDialog() }
    }

    override fun confirmAction(){
        if(selectedPlayer!=null){
            val json = JSONObject()
            json.put("TargetPlayer",selectedPlayer)
            onActionSubject.notify(GameUiEventSignal(GameUiEvent.HangedPlayer,json))
        }
        onActionSubject.notify(GameUiEventSignal(GameUiEvent.StartNextRound, JSONObject()))
    }

    override fun initGridLayout(){
        alivePlayers.forEach{
                player -> gridLayout.addView(createTextView(player.fetchPlayerName()),gridLayout.childCount)
        }
    }

}