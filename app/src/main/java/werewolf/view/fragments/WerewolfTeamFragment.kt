package werewolf.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.observer.Subject
import werewolf.model.entities.Player
import werewolf.view.GameUiEvent
import werewolf.view.R

class WerewolfTeamFragment(
    onActionSubject: Subject<GameUiEvent>,
    private val player: Player
) : PlayerGridFragment(onActionSubject,player){
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
        player.fetchTeammates().forEach{
                player -> teammatesGridLayout.addView(createTeammatesTextView(player.fetchPlayerName()),teammatesGridLayout.childCount)
        }
    }

    private fun createTeammatesTextView(playerName: String): TextView {
        val textView = TextView(requireContext()).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                setMargins(8, 8, 8, 8)
            }
            text = playerName
            textSize = 18f
            typeface = ResourcesCompat.getFont(context, R.font.font_old_english_five)
            setTextColor(Color.parseColor("#990000"))
            gravity = Gravity.CENTER
            setPadding(30,30,30,30)

            val scale = resources.displayMetrics.density
            minWidth = (100 * scale).toInt()
            minHeight = (50 * scale).toInt()
        }
        return textView
    }

}