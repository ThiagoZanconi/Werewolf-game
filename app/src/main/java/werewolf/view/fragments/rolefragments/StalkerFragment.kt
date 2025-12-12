package werewolf.view.fragments.rolefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.observer.Subject
import org.json.JSONObject
import werewolf.view.GameUiEventSignal
import werewolf.view.R
import werewolf.view.fragments.PlayerGridFragment

class StalkerFragment(
    onActionSubject: Subject<GameUiEventSignal>,
    jsonObject: JSONObject
) : PlayerGridFragment(onActionSubject,jsonObject){
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
        val stalkedPair = jsonObject.getString("StalkedPair")
        stalkedTextView = view.findViewById(R.id.textViewStalked)
        stalkedTextView.text = stalkedPair

    }

}