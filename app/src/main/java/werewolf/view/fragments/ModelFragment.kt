package werewolf.view.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import org.json.JSONArray
import org.json.JSONObject
import werewolf.model.Roles
import werewolf.model.entities.AbilityStateEnum
import werewolf.view.AbilityStateProvider
import werewolf.view.GameUiEvent
import werewolf.view.GameUiEventSignal
import werewolf.view.R
import werewolf.view.RoleNameProvider
import werewolf.view.howtoplay.RoleDescriptionProvider

abstract class ModelFragment: Fragment(){

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

    protected fun showConfirmActionDialog() {
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirmation, null)
        dialog.setContentView(view)
        dialog.show()

        val buttonYes: Button = view.findViewById(R.id.button_yes)
        val buttonNo: Button = view.findViewById(R.id.button_no)

        buttonYes.setOnClickListener {
            dialog.dismiss()
            confirmAction()
        }

        buttonNo.setOnClickListener {
            dialog.dismiss()
        }
    }

    protected abstract fun confirmAction()
}

open class PlayerFragment(protected val onActionSubject: Subject<GameUiEventSignal>, protected val jsonObject: JSONObject): ModelFragment() {

    protected lateinit var descriptionLabel: TextView
    protected lateinit var roleDescriptionButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player, container, false)
        initComponents(view)
        initListeners()
        return view
    }

    override fun confirmAction() {
        val jsonObject = JSONObject()
        jsonObject.put("Event",GameUiEvent.ConfirmAction)
        onActionSubject.notify(GameUiEventSignal(GameUiEvent.ConfirmAction, jsonObject))
    }

    override fun initComponents(view: View) {
        super.initComponents(view)
        descriptionLabel = view.findViewById(R.id.descriptionLabel)
        descriptionLabel.text = AbilityStateProvider.getAbilityState(AbilityStateEnum.valueOf(jsonObject.getString("AbilityState")))
        roleDescriptionButton = view.findViewById(R.id.roleDescriptionButton)
        titleLabel.text = RoleNameProvider.getRoleName(Roles.valueOf(jsonObject.getString("Role")))
        imageView.setImageResource(jsonObject.getInt("ImageSrc"))
    }

    override fun initListeners() {
        super.initListeners()
        roleDescriptionButton.setOnClickListener{ showRoleDescription() }
    }

    private fun showRoleDescription(){
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_description, null)
        dialog.setContentView(view)
        dialog.show()

        val textView: TextView = view.findViewById(R.id.roleDescriptionTextView)
        textView.text = getDescription()
    }

    protected open fun getDescription(): String{
        return RoleDescriptionProvider.getRoleDescription(Roles.valueOf(jsonObject.getString("Role")))
    }

}

open class AbilityPlayerFragment(onActionSubject: Subject<GameUiEventSignal>, jsonObject: JSONObject): PlayerFragment(onActionSubject,jsonObject){
    protected lateinit var abilityButton: TextView
    protected lateinit var abilitySelectedButton: TextView
    private var abilityUsed: Boolean = false

    override fun confirmAction(){
        if(abilityUsed){
            val json = JSONObject()
            json.put("PlayerName", jsonObject.getString("PlayerName"))
            json.put("TargetPlayers",JSONArray(listOf<String>()))
            json.put("Event",GameUiEvent.AbilityUsed)
            onActionSubject.notify(GameUiEventSignal(GameUiEvent.AbilityUsed, json))
        }
        else{
            super.confirmAction()
        }

    }

    override fun initComponents(view: View) {
        super.initComponents(view)
        initAbilityButton(view)
        initAbilitySelectedButton(view)
    }

    protected open fun initAbilityButton(view: View){
        abilityButton = view.findViewById(R.id.abilityTextView)
        abilityButton.setOnClickListener{ abilityButtonOnClickListener() }
    }

    protected open fun initAbilitySelectedButton(view: View){
        abilitySelectedButton = view.findViewById(R.id.abilitySelectedTextView)
        abilitySelectedButton.setOnClickListener{ abilitySelectedButtonOnClickListener() }
    }

    private fun abilityButtonOnClickListener(){
        abilityUsed = true
        abilityButton.visibility = View.GONE
        abilitySelectedButton.visibility = View.VISIBLE
    }

    private fun abilitySelectedButtonOnClickListener(){
        abilityUsed = false
        abilitySelectedButton.visibility = View.GONE
        abilityButton.visibility = View.VISIBLE
    }

}

abstract class GridFragment(onActionSubject: Subject<GameUiEventSignal>, jsonObject: JSONObject): PlayerFragment(onActionSubject, jsonObject) {

    protected lateinit var gridLayout: GridLayout
    protected var selectedPlayer: String? = null

    override fun initComponents(view: View) {
        super.initComponents(view)
        gridLayout = view.findViewById(R.id.gridLayout)
        initGridLayout()
    }

    private fun defineSelectedPlayer(playerName: String){
        selectedPlayer = playerName
    }

    protected abstract fun initGridLayout()

    protected open fun createTextView(playerName: String): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_client_player_container, gridLayout, false)
        val playerNameTextView = view.findViewById<TextView>(R.id.playerNameTextView)
        playerNameTextView.text = playerName
        playerNameTextView.setOnClickListener{ onPlayerClick(playerNameTextView, playerName) }

        return view
    }

    protected open fun onPlayerClick(textView: TextView, playerName: String) {
        if (textView.currentTextColor == Color.WHITE) {
            textView.setTextColor(Color.RED)
            markNotSelected(playerName)
            defineSelectedPlayer(playerName)
        } else {
            textView.setTextColor(Color.WHITE)
            selectedPlayer = null
        }
    }

    protected open fun markNotSelected(playerName: String) {
        for (i in 0 until gridLayout.childCount) {
            val view = gridLayout.getChildAt(i)
            val textView = view.findViewById<TextView>(R.id.playerNameTextView)
            if (textView.text != playerName) {
                textView.setTextColor(Color.WHITE)
            }
        }
    }
}

open class PlayerGridFragment(
    onActionSubject: Subject<GameUiEventSignal>,
    jsonObject: JSONObject
): GridFragment(onActionSubject, jsonObject){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player_grid, container, false)
        initComponents(view)
        initListeners()
        return view
    }

    override fun confirmAction(){
        val json = JSONObject()
        if(selectedPlayer!=null){
            json.put("PlayerName", jsonObject.getString("PlayerName"))
            json.put("TargetPlayers",JSONArray(listOf(selectedPlayer!!)))
            json.put("Event", GameUiEvent.AbilityUsed)
            onActionSubject.notify(GameUiEventSignal(GameUiEvent.AbilityUsed,json))
        }
        else{
            json.put("Event", GameUiEvent.ConfirmAction)
            onActionSubject.notify(GameUiEventSignal(GameUiEvent.ConfirmAction, json))
        }
    }

    override fun initGridLayout(){
        val jsonArray = jsonObject.getJSONArray("PossibleTargetPlayers")
        val possiblePlayerTargets = List(jsonArray.length()) { i ->
            jsonArray.getString(i)
        }
        possiblePlayerTargets.forEach{
                playerName -> gridLayout.addView(createTextView(playerName),gridLayout.childCount)
        }
    }

    private fun showRoleDescription(){
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_description, null)
        dialog.setContentView(view)
        dialog.show()

        val textView: TextView = view.findViewById(R.id.roleDescriptionTextView)
        textView.text = RoleDescriptionProvider.getRoleDescription(Roles.valueOf(jsonObject.getString("Role")))
    }
}