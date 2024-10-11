package werewolf.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.observer.Observable
import com.example.observer.Subject

interface InitActivity{
    val uiEventObservable: Observable<InitUiEvent>

    fun addPlayer(text:String)
    fun removePlayer(index: Int)
    fun startGame()
    fun getEditTextName(): String
    fun getPlayerToRemove(): String
}

class InitActivityImpl : AppCompatActivity(), InitActivity {
    private val ROW_SIZE = 3

    private val onActionSubject = Subject<InitUiEvent>()

    private lateinit var addPlayerButton: Button
    private lateinit var startGameButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var nameLabelTextView: TextView
    private lateinit var gridLayout: GridLayout

    override val uiEventObservable: Observable<InitUiEvent> = onActionSubject

    private var playerToRemove : String =""

    override fun addPlayer(text:String) {
        val container = createContainer(text)
        adjustGridLayout()
        gridLayout.addView(container,gridLayout.childCount)
        nameEditText.setText("")
    }

    override fun removePlayer(index:Int){
        gridLayout.removeViewAt(index)
    }

    override fun startGame() {
        val intent = Intent(applicationContext, GameActivityImpl::class.java)
        startActivity(intent)
    }

    override fun getEditTextName(): String {
        return nameEditText.text.toString()
    }

    override fun getPlayerToRemove(): String {
        return playerToRemove
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initModule()
        initComponents()
        initListeners()
    }

    private fun initModule() {
        ViewInjector.init(this)
    }

    private fun initComponents(){
        addPlayerButton = findViewById(R.id.addButton)
        startGameButton = findViewById(R.id.startButton)
        nameEditText = findViewById(R.id.termEditText)
        nameLabelTextView = findViewById(R.id.nameTextView)
        gridLayout = findViewById(R.id.gridLayout)
        gridLayout.rowCount = 3
        gridLayout.columnCount = ROW_SIZE
    }

    private fun initListeners(){
        addPlayerButton.setOnClickListener{ notifyAddPlayerAction() }
        startGameButton.setOnClickListener{ notifyStartGameAction() }
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
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                setMargins(8, 8, 8, 8)
            }
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(16, 16, 16, 16)
            this.setBackgroundResource(R.drawable.textview_shape2)
        }
        return linearLayout
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(this).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                setMargins(8, 8, 8, 8)
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

    private fun adjustGridLayout(){
        if(gridLayout.rowCount*gridLayout.columnCount<=gridLayout.childCount){
            gridLayout.rowCount = gridLayout.rowCount + 3
        }
    }
}