package werewolf.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.observer.Observable
import com.example.observer.Subject
import werewolf.model.GameSettingsImpl
import werewolf.view.InitUiEvent
import werewolf.view.InitUiSignal
import werewolf.view.R

interface ServerInterface{
    val uiEventObservable: Observable<InitUiSignal>

    fun addLocalPlayer(text: String)
    fun addConnectedPlayer(text: String)
    fun removePlayer(index: Int, name: String)
    fun startGame()
}

class ServerFragment: Fragment(), ServerInterface {
    private lateinit var addPlayerButton: Button
    private lateinit var startGameButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var tvServerIpTextView: TextView
    private lateinit var gridLayout: GridLayout

    private val onActionSubject = Subject<InitUiSignal>()
    override val uiEventObservable: Observable<InitUiSignal> = onActionSubject

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player_selection, container, false)
        initComponents(view)
        initListeners()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(GameSettingsImpl.size()>=3){
            loadLocalPlayers()
        }
    }

    override fun addLocalPlayer(text:String) {
        val container = createLocalPlayerContainer(text)
        gridLayout.addView(container,gridLayout.childCount)
        nameEditText.setText("")
    }

    override fun addConnectedPlayer(text: String) {
        val container = createConnectedPlayerContainer(text)
        gridLayout.addView(container,gridLayout.childCount)
    }

    override fun removePlayer(index:Int, name: String){
        gridLayout.removeViewAt(index)
    }

    override fun startGame() {
        hideKeyboard()
        initSettingsFragment()
    }

    private fun initComponents(view: View){
        tvServerIpTextView = view.findViewById(R.id.tvServerIp)
        addPlayerButton = view.findViewById(R.id.addButton)
        startGameButton = view.findViewById(R.id.startButton)
        nameEditText = view.findViewById(R.id.termEditText)
        gridLayout = view.findViewById(R.id.gridLayout)
    }

    private fun initListeners(){
        addPlayerButton.setOnClickListener{ notifyAddLocalPlayerAction() }
        startGameButton.setOnClickListener{ notifyStartGameAction() }
    }

    private fun notifyAddConnectedPlayerAction(name: String){
        onActionSubject.notify(InitUiSignal(InitUiEvent.AddConnectedPlayer, name))
    }

    private fun notifyAddLocalPlayerAction(){
        onActionSubject.notify(InitUiSignal(InitUiEvent.AddLocalPlayer, nameEditText.text.toString()))
    }

    private fun notifyRemovePlayerAction(name: String){
        onActionSubject.notify(InitUiSignal(InitUiEvent.RemovePlayer,name))
    }

    private fun notifyStartGameAction(){
        onActionSubject.notify(InitUiSignal(InitUiEvent.StartGame))
    }

    private fun loadLocalPlayers(){
        val localPlayers = GameSettingsImpl.fetchLocalPlayersAndRemoveClientPlayers()
        localPlayers.forEach {
            addLocalPlayer(it)
        }
    }

    private fun initSettingsFragment(){
        parentFragmentManager.beginTransaction()
            .replace(R.id.OptionFragment, ServerSettingsFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun createLocalPlayerContainer(text: String): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_local_player_name_container, gridLayout, false)
        val playerNameTextView = view.findViewById<TextView>(R.id.playerNameTextView)
        playerNameTextView.text = text
        val removePlayerButton = view.findViewById<TextView>(R.id.removePlayerButton)
        removePlayerButton.setOnClickListener{ notifyRemovePlayerAction(text) }
        return view
    }

    private fun createConnectedPlayerContainer(text: String): View{
        val view = LayoutInflater.from(context).inflate(R.layout.item_client_player_container, gridLayout, false)
        val playerNameTextView = view.findViewById<TextView>(R.id.playerNameTextView)
        playerNameTextView.text = text

        return view
    }

}