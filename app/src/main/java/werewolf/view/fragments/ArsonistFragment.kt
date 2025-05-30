package werewolf.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import com.example.observer.Subject
import werewolf.model.entities.werewolves.Arsonist
import werewolf.view.GameUiEvent
import werewolf.view.R

class ArsonistFragment(
    onActionSubject: Subject<GameUiEvent>,
    private val player: Arsonist
) : PlayerGridFragment(onActionSubject,player){
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

    override fun createTextView(playerName: String): TextView {
        val textView = TextView(requireContext()).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                setMargins(8, 8, 8, 8)
            }
            this.text = playerName
            textSize = 18f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(30,30,30,30)

            val scale = resources.displayMetrics.density
            minWidth = (100 * scale).toInt()
            minHeight = (50 * scale).toInt()

            setOnClickListener {
                if (this.background == null) {
                    this.setBackgroundResource(R.drawable.imageview_shape)
                    markNotSelected(playerName)
                    igniteSelectedTextViewOnClickListener()
                    setSelectedPlayer(playerName)
                } else {
                    this.background = null
                    selectedPlayer = null
                }
            }
        }
        return textView
    }

    private fun initIgniteTextView(view: View){
        igniteTextView = view.findViewById(R.id.igniteTextView)
        igniteTextView.setOnClickListener { igniteTextViewOnClickListener() }

    }

    private fun initIgniteSelectedTextView(view: View){
        igniteSelectedTextView = view.findViewById(R.id.igniteSelectedTextView)
        igniteSelectedTextView.setOnClickListener { igniteSelectedTextViewOnClickListener() }
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