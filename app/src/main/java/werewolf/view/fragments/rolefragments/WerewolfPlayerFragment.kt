package werewolf.view.fragments.rolefragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import com.example.observer.Subject
import org.json.JSONObject
import werewolf.view.GameUiEventSignal
import werewolf.view.R
import werewolf.view.fragments.PlayerGridFragment

open class WerewolfPlayerFragment(
    onActionSubject: Subject<GameUiEventSignal>,
    jsonObject: JSONObject,
) : PlayerGridFragment(onActionSubject,jsonObject){
    private lateinit var teammatesGridLayout: GridLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_werewolf_teammates, container, false)
        initComponents(view)
        initListeners()

        return view
    }

    override fun initComponents(view: View) {
        super.initComponents(view)
        teammatesGridLayout = view.findViewById(R.id.teammatesGridLayout)
        initTeammatesGridLayout()
    }

    private fun initTeammatesGridLayout(){
        val jsonArray = jsonObject.getJSONArray("Teammates")
        val teammates = List(jsonArray.length()) { i ->
            jsonArray.getString(i)
        }
        teammates.forEach{
                playerName -> teammatesGridLayout.addView(createTextView(playerName),teammatesGridLayout.childCount)
        }
    }

    override fun markNotSelected(playerName: String) {
        super.markNotSelected(playerName)
        for (i in 0 until teammatesGridLayout.childCount) {
            val view = teammatesGridLayout.getChildAt(i)
            val textView = view.findViewById<TextView>(R.id.playerNameTextView)
            if (textView.text != playerName) {
                textView.setTextColor(Color.WHITE)
            }
        }
    }

}