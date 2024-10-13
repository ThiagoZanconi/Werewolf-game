package werewolf.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.observer.Observable
import com.example.observer.Subject
import werewolf.model.entities.Player
import werewolf.view.fragments.FinishedGameFragment
import werewolf.view.fragments.FinishedRoundFragment
import werewolf.view.fragments.WinnerTeam

interface GameActivity{
    val uiEventObservable: Observable<GameUiEvent>

    fun setCurrentPlayer(player: String)
    fun startTurn(player: Player)
    fun finishRound(text: String, alivePlayers: List<Player>)
    fun gameFinished(winners:List<Player>, winnerTeam:WinnerTeam, gameLogs: String)
}

class GameActivityImpl: AppCompatActivity(), GameActivity{
    private val onActionSubject = Subject<GameUiEvent>()

    private lateinit var timerTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var startTurnButton: Button

    override val uiEventObservable: Observable<GameUiEvent> = onActionSubject

    override fun setCurrentPlayer(player: String) {
        turnOnCover()
        clearAllFragments()
        nameTextView.text = player
    }

    override fun startTurn(player: Player) {
        startTimer(player.fetchView(onActionSubject))
    }

    override fun finishRound(text: String, alivePlayers: List<Player>) {
        initFragment(FinishedRoundFragment(onActionSubject,text,alivePlayers))
    }

    override fun gameFinished(winners:List<Player>, winnerTeam:WinnerTeam, gameLogs: String) {
        initFragment(FinishedGameFragment(onActionSubject,winners,winnerTeam, gameLogs))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        initComponents()
        initListeners()
        initModule()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }

    private fun initModule(){
        ViewInjector.initGameActivity(this)
    }

    private fun initComponents(){
        timerTextView = findViewById(R.id.timerTextView)
        nameTextView = findViewById(R.id.playerNameLabel)
        startTurnButton = findViewById(R.id.startButton)
    }

    private fun initListeners(){
        startTurnButton.setOnClickListener{ startTurnEvent() }
    }

    private fun startTurnEvent(){
        onActionSubject.notify(GameUiEvent.StartTurn)
    }

    private fun startTimer(fragment: Fragment) {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                initFragment(fragment)
                turnOffCover()
            }
        }.start()
    }

    private fun initFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.RoleFragment, fragment)
            .commit()
    }

    private fun turnOffCover(){
        startTurnButton.visibility = View.GONE
        timerTextView.visibility = View.GONE
        timerTextView.text = ""
    }

    private fun turnOnCover(){
        startTurnButton.visibility = View.VISIBLE
        timerTextView.visibility = View.VISIBLE
    }

    private fun clearAllFragments() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        for (fragment in fragmentManager.fragments) {
            if (fragment != null) {
                transaction.remove(fragment)
            }
        }
        transaction.commitNow()
    }
}