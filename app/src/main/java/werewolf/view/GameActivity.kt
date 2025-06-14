package werewolf.view

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.observer.Observable
import com.example.observer.Subject
import werewolf.model.entities.Player
import werewolf.view.fragments.FinishedGameFragment
import werewolf.view.fragments.FinishedRoundFragment
import werewolf.view.fragments.WinnerTeam
import java.util.Locale

interface GameActivity{
    val uiEventObservable: Observable<GameUiEvent>
    val targetPlayersObservable: Observable<TargetPlayersSignal>

    fun setCurrentPlayer(player: String)
    fun startTurn(player: Player)
    fun finishRound(text: String, alivePlayers: List<Player>)
    fun gameFinished(winners:List<Player>, winnerTeam:WinnerTeam, gameLogs: String)
}

class GameActivityImpl: AppCompatActivity(), GameActivity{
    private lateinit var timerTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var startTurnButton: Button
    private lateinit var mediaPlayer: MediaPlayer

    private val onActionSubject = Subject<GameUiEvent>()
    override val uiEventObservable: Observable<GameUiEvent> = onActionSubject

    private val targetPlayersOnActionSubject = Subject<TargetPlayersSignal>()
    override val targetPlayersObservable: Observable<TargetPlayersSignal> = targetPlayersOnActionSubject

    override fun setCurrentPlayer(player: String) {
        turnOnCover()
        clearAllFragments()
        nameTextView.text = player
    }

    override fun startTurn(player: Player) {
        startTurnButton.isEnabled = false
        startTimer(player.fetchView(onActionSubject, targetPlayersOnActionSubject))
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
        initMediaPlayer()
        onBackPressedDispatcher.addCallback(this) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
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

    private fun initMediaPlayer(){
        mediaPlayer = MediaPlayer.create(this, R.raw.haunted_forest_ambient_martias_muses)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    private fun startTurnEvent(){
        onActionSubject.notify(GameUiEvent.StartTurn)
    }

    private fun startTimer(fragment: Fragment) {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = String.format(Locale.getDefault(), "%d", millisUntilFinished / 1000)
            }

            override fun onFinish() {
                initFragment(fragment)
                turnOffCover()
                startTurnButton.isEnabled = true
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