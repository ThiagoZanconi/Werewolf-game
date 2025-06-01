package werewolf.view.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.observer.Subject
import werewolf.model.entities.Player
import werewolf.view.GameUiEvent
import werewolf.view.R
import werewolf.view.TargetPlayersSignal
import werewolf.view.howtoplay.RoleDescriptionProvider

abstract class FragmentModel: Fragment(){

    protected lateinit var confirmButton: Button
    protected lateinit var imageView: ImageView
    protected lateinit var titleLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })
    }

    protected open fun initComponents(view: View) {
        confirmButton = view.findViewById(R.id.confirmButton)
        imageView = view.findViewById(R.id.roleImageLabel)
        titleLabel = view.findViewById(R.id.titleLabel)
    }

    protected open fun initListeners(){
        confirmButton.setOnClickListener { showConfirmActionDialog() }
    }

    private fun showConfirmActionDialog() {
        val dialog = Dialog(requireContext())

        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirmation, null)
        dialog.setContentView(view)

        val textView: TextView = view.findViewById(R.id.text_question)
        val buttonYes: Button = view.findViewById(R.id.button_yes)
        val buttonNo: Button = view.findViewById(R.id.button_no)

        textView.text = requireContext().getString(R.string.proceed)

        buttonYes.setOnClickListener {
            confirmAction()
            dialog.dismiss()
        }

        buttonNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    protected abstract fun confirmAction()
}

abstract class GridFragment: FragmentModel() {

    protected lateinit var gridLayout: GridLayout
    protected var selectedPlayer: Player? = null

    override fun initComponents(view: View) {
        super.initComponents(view)
        gridLayout = view.findViewById(R.id.gridLayout)
        initGridLayout()
    }

    protected abstract fun initGridLayout()

    protected abstract fun setSelectedPlayer(playerName: String)

    protected open fun createTextView(playerName: String): TextView {
        val textView = TextView(requireContext()).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                setMargins(8, 8, 8, 8)
            }
            text = playerName
            textSize = 18f
            typeface = ResourcesCompat.getFont(context, R.font.font_old_english_five)
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
            setPadding(30,30,30,30)

            val scale = resources.displayMetrics.density
            minWidth = (100 * scale).toInt()
            minHeight = (50 * scale).toInt()

            setOnClickListener {
                onPlayerClick(this, playerName)
            }
        }
        return textView
    }

    protected open fun onPlayerClick(textView: TextView, playerName: String) {
        if (textView.background == null) {
            textView.setBackgroundResource(R.drawable.imageview_shape)
            markNotSelected(playerName)
            setSelectedPlayer(playerName)
        } else {
            textView.background = null
            selectedPlayer = null
        }
    }

    protected fun markNotSelected(playerName: String) {
        for (i in 0 until gridLayout.childCount) {
            val child = gridLayout.getChildAt(i) as TextView
            if (child.text != playerName) {
                child.background = null
            }
        }
    }
}

open class PlayerGridFragment(
    protected val onActionSubject: Subject<GameUiEvent>,
    private val player: Player,
    protected val targetPlayersOnActionSubject: Subject<TargetPlayersSignal>
): GridFragment(){
    private lateinit var abilityStateLabel: TextView
    private lateinit var roleDescriptionButton: ImageButton
    private lateinit var roleDescriptionLabel: TextView
    protected val targetPlayersSignal: TargetPlayersSignal = TargetPlayersSignal(player.fetchTargetPlayers())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_grid, container, false)
        initComponents(view)
        initListeners()
        targetPlayersOnActionSubject.notify(targetPlayersSignal)
        return view
    }

    override fun initComponents(view: View) {
        super.initComponents(view)
        abilityStateLabel = view.findViewById(R.id.descriptionLabel)
        abilityStateLabel.text = player.fetchAbilityState()
        roleDescriptionButton = view.findViewById(R.id.roleDescriptionButton)
        roleDescriptionLabel = view.findViewById(R.id.roleDescription)
        roleDescriptionLabel.text = RoleDescriptionProvider.getRoleDescription(player.fetchRole())
        titleLabel.text = player.fetchRole().toString()
        imageView.setImageResource(player.fetchImageSrc())
    }

    override fun initListeners() {
        super.initListeners()
        roleDescriptionButton.setOnClickListener{ showRoleDescription() }
    }

    override fun confirmAction(){
        if(selectedPlayer!=null){
            player.notifyAbilityUsed(selectedPlayer)
        }
        onActionSubject.notify(GameUiEvent.ConfirmAction)
    }

    override fun initGridLayout(){
        targetPlayersSignal.targetPlayers.forEach{
                player -> gridLayout.addView(createTextView(player.fetchPlayerName()),gridLayout.childCount)
        }
    }

    override fun setSelectedPlayer(playerName: String){
        selectedPlayer = targetPlayersSignal.targetPlayers.find { it.fetchPlayerName() == playerName }
    }

    private fun showRoleDescription(){
        if(roleDescriptionLabel.visibility==View.VISIBLE){
            roleDescriptionLabel.visibility=View.GONE
        }
        else{
            roleDescriptionLabel.visibility=View.VISIBLE
        }
    }
}