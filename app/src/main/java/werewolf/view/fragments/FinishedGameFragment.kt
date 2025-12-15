package werewolf.view.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.example.observer.Subject
import org.json.JSONObject
import werewolf.view.GameUiEventSignal
import werewolf.view.InitActivityImpl
import werewolf.view.R

enum class WinnerTeam{
    WEREWOLVES, VILLAGERS, DRAW, JESTER
}

class FinishedGameFragment(
    onActionSubject: Subject<GameUiEventSignal>,
    jsonObject: JSONObject
):GridFragment(onActionSubject, jsonObject){
    private lateinit var gameLogsButton: Button
    private lateinit var playAgainButton: Button
    private lateinit var roleDescriptionButtonContainer: FrameLayout

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
        confirmButton = view.findViewById(R.id.confirmButton)
        imageView = view.findViewById(R.id.roleImageLabel)
        titleLabel = view.findViewById(R.id.titleLabel)
        gameLogsButton = view.findViewById(R.id.gameLogsButton)
        playAgainButton = view.findViewById(R.id.playAgainButton)
        roleDescriptionButton = view.findViewById(R.id.roleDescriptionButton)
        descriptionLabel = view.findViewById(R.id.descriptionLabel)
        roleDescriptionButtonContainer = view.findViewById(R.id.roleDescriptionButtonContainer)
        gridLayout = view.findViewById(R.id.gridLayout)
        initGridLayout()
        roleDescriptionButtonContainer.visibility = View.GONE
        roleDescriptionButton.visibility = View.GONE
        descriptionLabel.text = requireContext().getString(R.string.winners)
        val winnerTeam = WinnerTeam.valueOf(jsonObject.getString("WinnerTeam"))
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
        println(jsonObject)
        val jsonArray = jsonObject.getJSONArray("WinnerPlayerList")
        val winners = List(jsonArray.length()) { i ->
            jsonArray.getString(i)
        }
        winners.forEach{
                playerName -> gridLayout.addView(createTextView(playerName),gridLayout.childCount)
        }
    }

    override fun onPlayerClick(textView: TextView, playerName: String) {}

    private fun showGameLogsDialog(){
        val dialog = Dialog(requireContext())
        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_gamelogs, null)
        val textView: TextView = view.findViewById(R.id.gameLogsLabel)
        textView.text = jsonObject.getString("GameLogs")
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