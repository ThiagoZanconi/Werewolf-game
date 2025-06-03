package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.observer.Subject
import werewolf.model.entities.villagers.Stalker
import werewolf.view.GameUiEvent
import werewolf.view.R
import werewolf.view.TargetPlayersSignal

class StalkerFragment(
    onActionSubject: Subject<GameUiEvent>,
    private val stalker: Stalker,
    targetPlayersOnActionSubject: Subject<TargetPlayersSignal>
) : PlayerGridFragment(onActionSubject,stalker,targetPlayersOnActionSubject){
    private lateinit var stalkedTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stalker, container, false)
        initComponents(view)
        initListeners()

        return view
    }

    override fun initComponents(view: View) {
        super.initComponents(view)
        val stalkedPair = stalker.fetchStalkedPair()
        val text = stalkedPair?.first?.fetchPlayerName() ?: ""
        stalkedTextView = view.findViewById(R.id.textViewStalked)
        if (stalkedPair != null) {
            stalkedTextView.text = getString(R.string.saw, text, stalkedPair.second)
        }
    }

}