package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.observer.Subject
import werewolf.model.entities.Player
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
    private val oilTargets: LinkedHashMap<Player,TextView> = LinkedHashMap()

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
        else if(oilTargets.keys.isNotEmpty()){
            player.setOilTargets(oilTargets.keys.toList())
            player.notifyAbilityUsed(selectedPlayer)
        }
        onActionSubject.notify(GameUiEvent.ConfirmAction)
    }

    override fun onPlayerClick(textView: TextView, playerName: String) {
        igniteSelectedTextViewOnClickListener()
        if (textView.background == null) {
            if(oilTargets.size==2){
                val deleted = oilTargets.remove(oilTargets.keys.first())
                deleted!!.background=null
                textView.setBackgroundResource(R.drawable.imageview_shape)
                oilTargets[getPlayer(playerName)] = textView
            }
            else{
                textView.setBackgroundResource(R.drawable.imageview_shape)
                oilTargets[getPlayer(playerName)] = textView
            }
        } else {
            textView.background = null
            oilTargets.remove(getPlayer(playerName))
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
        oilTargets.clear()
        igniteTextView.visibility=View.GONE
        igniteSelectedTextView.visibility=View.VISIBLE
    }

    private fun igniteSelectedTextViewOnClickListener(){
        ignite = false
        igniteSelectedTextView.visibility=View.GONE
        igniteTextView.visibility=View.VISIBLE
    }

    private fun getPlayer(name: String): Player{
        return targetPlayersSignal.targetPlayers.find { it.fetchPlayerName() == name }!!
    }
}