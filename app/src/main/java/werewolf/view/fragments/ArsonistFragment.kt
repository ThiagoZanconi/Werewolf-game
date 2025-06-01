package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.observer.Subject
import werewolf.model.entities.werewolves.Arsonist
import werewolf.view.GameUiEvent
import werewolf.view.R
import werewolf.view.TargetPlayersSignal

class ArsonistFragment(
    onActionSubject: Subject<GameUiEvent>,
    private val player: Arsonist,
    targetPlayersOnActionSubject: Subject<TargetPlayersSignal>
) : PlayerGridFragment(onActionSubject,player,targetPlayersOnActionSubject){
    private lateinit var igniteTextView: TextView
    private lateinit var igniteSelectedTextView: TextView
    private var ignite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_arsonist, container, false)
        initComponents(view)
        initListeners()

        return view
    }

    override fun initComponents(view: View) {
        super.initComponents(view)
        initIgniteTextView(view)
        initIgniteSelectedTextView(view)
    }

    override fun confirmAction(){
        player.setIgnite(ignite)
        if(ignite){
            player.notifyAbilityUsed(null)
        }
        super.confirmAction()
    }

    override fun onPlayerClick(textView: TextView, playerName: String) {
        if (textView.background == null) {
            textView.setBackgroundResource(R.drawable.imageview_shape)
            markNotSelected(playerName)
            igniteSelectedTextViewOnClickListener()
            setSelectedPlayer(playerName)
        } else {
            textView.background = null
            selectedPlayer = null
        }
    }

    private fun initIgniteTextView(view: View){
        igniteTextView = view.findViewById(R.id.igniteTextView)
        igniteTextView.setOnClickListener{ igniteTextViewOnClickListener() }
    }

    private fun initIgniteSelectedTextView(view: View){
        igniteSelectedTextView = view.findViewById(R.id.igniteSelectedTextView)
        igniteSelectedTextView.setOnClickListener{ igniteSelectedTextViewOnClickListener() }
    }

    private fun igniteTextViewOnClickListener(){
        ignite = true
        markNotSelected("")
        igniteTextView.visibility=View.GONE
        igniteSelectedTextView.visibility=View.VISIBLE
    }

    private fun igniteSelectedTextViewOnClickListener(){
        ignite = false
        igniteSelectedTextView.visibility=View.GONE
        igniteTextView.visibility=View.VISIBLE
    }
}