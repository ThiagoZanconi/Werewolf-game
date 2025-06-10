package werewolf.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.observer.Observable
import com.example.observer.Subject
import werewolf.view.fragments.HowToPlayFragment
import werewolf.view.fragments.SettingsFragment
import werewolf.model.GameSettingsImpl

interface InitActivity{
    val uiEventObservable: Observable<InitUiEvent>

    fun addPlayer(text:String)
    fun removePlayer(index: Int, name: String)
    fun startGame()
    fun getEditTextName(): String
    fun getPlayerToRemove(): String
}

class InitActivityImpl : AppCompatActivity(), InitActivity {

    private lateinit var addPlayerButton: Button
    private lateinit var startGameButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var nameLabelTextView: TextView
    private lateinit var gridLayout: GridLayout
    private lateinit var howToPlayButton: Button

    private val onActionSubject = Subject<InitUiEvent>()
    override val uiEventObservable: Observable<InitUiEvent> = onActionSubject

    private var playerToRemove : String =""

    override fun addPlayer(text:String) {
        val container = createContainer(text)
        gridLayout.addView(container,gridLayout.childCount)
        nameEditText.setText("")
    }

    override fun removePlayer(index:Int, name: String){
        gridLayout.removeViewAt(index)
    }

    override fun startGame() {
        hideKeyboard()
        initSettingsFragment()
    }

    override fun getEditTextName(): String {
        return nameEditText.text.toString()
    }

    override fun getPlayerToRemove(): String {
        return playerToRemove
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_main)

        initModule()
        initComponents()
        initListeners()
        if(GameSettingsImpl.size()>0) {
            initSettingsFragment()
        }
    }

    private fun initModule() {
        ViewInjector.init(this)
    }

    private fun initComponents(){
        addPlayerButton = findViewById(R.id.addButton)
        startGameButton = findViewById(R.id.startButton)
        nameEditText = findViewById(R.id.termEditText)
        nameLabelTextView = findViewById(R.id.teamTextView)
        gridLayout = findViewById(R.id.gridLayout)
        howToPlayButton = findViewById(R.id.howToPlayButton)
    }

    private fun initListeners(){
        addPlayerButton.setOnClickListener{ notifyAddPlayerAction() }
        startGameButton.setOnClickListener{ notifyStartGameAction() }
        howToPlayButton.setOnClickListener { goToHowPlay() }
    }

    private fun notifyAddPlayerAction(){
        onActionSubject.notify(InitUiEvent.AddPlayer)
    }

    private fun notifyRemovePlayerAction(){
        onActionSubject.notify(InitUiEvent.RemovePlayer)
    }

    private fun notifyStartGameAction(){
        onActionSubject.notify(InitUiEvent.StartGame)
    }

    private fun goToHowPlay(){
        initFragment(HowToPlayFragment())
    }

    private fun initFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.OptionFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun initSettingsFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.OptionFragment, SettingsFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun hideKeyboard(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun createContainer(text: String): LinearLayout {
        val linearLayout = createLinearLayout()
        val textView = createTextView(text)
        val button = createButton(text)

        linearLayout.addView(textView)
        linearLayout.addView(button)

        return linearLayout
    }

    private fun createLinearLayout():LinearLayout{
        val linearLayout = LinearLayout(this).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // peso 1
                marginEnd = 5
                marginStart = 5
                setGravity(Gravity.FILL);
                setMargins(8, 8, 8, 8)
            }
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(5, 5, 5, 5)
            this.setBackgroundResource(R.drawable.textview_shape2)
        }
        return linearLayout
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(this).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                setMargins(4, 4, 4, 4)
            }
            this.text = text
            textSize = 18f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }
        return textView
    }

    private fun createButton(text: String): Button {
        val button = Button(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            this.text = "X"
            setOnClickListener {
                playerToRemove = text
                notifyRemovePlayerAction()
            }
        }
        return button
    }
}