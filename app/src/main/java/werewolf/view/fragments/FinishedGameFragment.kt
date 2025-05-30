package werewolf.view.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.observer.Subject
import werewolf.model.entities.Player
import werewolf.view.GameUiEvent
import werewolf.view.InitActivityImpl
import werewolf.view.R

enum class WinnerTeam{
    WEREWOLVES, VILLAGERS, DRAW, JESTER
}

class FinishedGameFragment(
    private val onActionSubject: Subject<GameUiEvent>,
    private val winners:List<Player>,
    private val winnerTeam: WinnerTeam,
    private val gameLogs: String
):GridFragment(){
    private lateinit var gameLogsButton: Button
    private lateinit var playAgainButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_finishedgame, container, false)
        initComponents(view)
        initListeners()

        return view
    }

    override fun confirmAction() {
        requireActivity().finishAffinity()
    }

    override fun initComponents(view: View) {
        super.initComponents(view)
        gameLogsButton = view.findViewById(R.id.gameLogsButton)
        playAgainButton = view.findViewById(R.id.playAgainButton)

        when(winnerTeam){
            WinnerTeam.DRAW -> draw()
            WinnerTeam.WEREWOLVES -> werewolvesWin()
            WinnerTeam.VILLAGERS -> villagersWin()
            WinnerTeam.JESTER -> jesterWin()
        }
    }

    override fun initListeners() {
        super.initListeners()
        gameLogsButton.setOnClickListener { showGameLogsDialog() }
        playAgainButton.setOnClickListener { playAgain() }
    }

    override fun initGridLayout() {
        winners.forEach{
                player -> gridLayout.addView(createTextView(player.fetchPlayerName()),gridLayout.childCount)
        }
    }

    override fun createTextView(playerName: String): TextView {
        val textView = TextView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = playerName
            textSize = 18f
            typeface = ResourcesCompat.getFont(context, R.font.font_old_english_five)
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER

            val scale = resources.displayMetrics.density
            minWidth = (100 * scale).toInt()
            minHeight = (50 * scale).toInt()
        }
        return textView
    }

    override fun setSelectedPlayer(playerName: String) {}

    private fun showGameLogsDialog(){
        val dialog = Dialog(requireContext())
        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_gamelogs, null)
        val textView: TextView = view.findViewById(R.id.gameLogsLabel)
        textView.text = gameLogs
        dialog.setContentView(view)
        dialog.show()
    }

    private fun draw(){
        imageView.setImageResource(R.drawable.draw)
        titleLabel.text = requireContext().getString(R.string.draw)
    }

    private fun villagersWin(){
        imageView.setImageResource(R.drawable.villagers)
        titleLabel.text = requireContext().getString(R.string.villagers_win)
    }

    private fun werewolvesWin(){
        imageView.setImageResource(R.drawable.werewolves)
        titleLabel.text = requireContext().getString(R.string.werewolves_win)
    }

    private fun jesterWin(){
        imageView.setImageResource(R.drawable.jester)
        titleLabel.text = requireContext().getString(R.string.jester_wins)
    }

    private fun playAgain(){
        val intent = Intent(requireActivity().applicationContext, InitActivityImpl::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}