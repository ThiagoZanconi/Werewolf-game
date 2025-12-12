package werewolf.view.fragments.rolefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.observer.Subject
import org.json.JSONArray
import org.json.JSONObject
import werewolf.view.GameUiEvent
import werewolf.view.GameUiEventSignal
import werewolf.view.R

class ArsonistFragment(
    onActionSubject: Subject<GameUiEventSignal>,
    jsonObject: JSONObject
) : WerewolfPlayerFragment(onActionSubject, jsonObject){
    private lateinit var igniteTextView: TextView
    private lateinit var igniteSelectedTextView: TextView
    private var ignite: Boolean = false
    private val oilTargets: LinkedHashMap<String,TextView> = LinkedHashMap()

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
        val json = JSONObject()
        if(ignite){
            json.put("PlayerName", jsonObject.getString("PlayerName"))
            json.put("TargetPlayers",JSONArray(listOf<String>()))
            json.put("Event", GameUiEvent.AbilityUsed)
            onActionSubject.notify(GameUiEventSignal(GameUiEvent.AbilityUsed,json))
        }
        else if(oilTargets.keys.isNotEmpty()){
            json.put("PlayerName", jsonObject.getString("PlayerName"))
            json.put("TargetPlayers",JSONArray(oilTargets.keys.toString()))
            json.put("Event", GameUiEvent.AbilityUsed)
            onActionSubject.notify(GameUiEventSignal(GameUiEvent.AbilityUsed,json))
        }
        else{
            json.put("Event", GameUiEvent.ConfirmAction)
            onActionSubject.notify(GameUiEventSignal(GameUiEvent.ConfirmAction, json))
        }
    }

    override fun onPlayerClick(textView: TextView, playerName: String) {
        igniteSelectedTextViewOnClickListener()
        if (textView.background == null) {
            if(oilTargets.size==2){
                val deleted = oilTargets.remove(oilTargets.keys.first())
                deleted!!.background=null
                textView.setBackgroundResource(R.drawable.imageview_shape)
                oilTargets[playerName] = textView
            }
            else{
                textView.setBackgroundResource(R.drawable.imageview_shape)
                oilTargets[playerName] = textView
            }
        } else {
            textView.background = null
            oilTargets.remove(playerName)
        }
    }

    private fun initIgniteTextView(view: View){
        igniteTextView = view.findViewById(R.id.abilityTextView)
        igniteTextView.setOnClickListener{ igniteTextViewOnClickListener() }
    }

    private fun initIgniteSelectedTextView(view: View){
        igniteSelectedTextView = view.findViewById(R.id.abilitySelectedTextView)
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

}