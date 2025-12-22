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
import androidx.lifecycle.lifecycleScope
import com.example.observer.Observable
import com.example.observer.Subject
import org.json.JSONObject
import werewolf.model.entities.Player
import werewolf.view.fragments.FinishedGameFragment
import werewolf.view.fragments.FinishedRoundFragment
import java.util.Locale

interface GameActivity{
    val uiEventObservable: Observable<GameUiEventSignal>
    fun setCurrentPlayer(player: String)
    fun startTurn(jsonObject: JSONObject)
    fun finishRound(text: String, alivePlayers: List<Player>)
    fun gameFinished(jsonObject: JSONObject)
}

class GameActivityImpl: AppCompatActivity(), GameActivity{
    private lateinit var timerTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var startTurnButton: Button
    private lateinit var mediaPlayer: MediaPlayer

    private val onActionSubject = Subject<GameUiEventSignal>()
    override val uiEventObservable: Observable<GameUiEventSignal> = onActionSubject

    override fun setCurrentPlayer(player: String) {
        nameTextView.text = player
        turnOnCover()
        clearAllFragments()
        startTurnButton.isEnabled = true
    }

    override fun startTurn(jsonObject: JSONObject) {
        startTurnButton.isEnabled = false
        val fragment = FragmentProvider.getFragment(onActionSubject, jsonObject)
        startTimer(fragment)
    }

    override fun finishRound(text: String, alivePlayers: List<Player>) {
        initFragment(FinishedRoundFragment(onActionSubject,text,alivePlayers))
    }

    override fun gameFinished(jsonObject: JSONObject) {
        initFragment(FinishedGameFragment(onActionSubject, jsonObject))
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
        ViewInjector.initServerGameFragment(this, lifecycleScope)
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
        onActionSubject.notify(GameUiEventSignal(GameUiEvent.StartTurn, JSONObject()))
    }

    private fun startTimer(fragment: Fragment) {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = String.format(Locale.getDefault(), "%d", millisUntilFinished / 1000)
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