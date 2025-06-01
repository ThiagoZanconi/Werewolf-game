package werewolf.view.fragments

import com.example.observer.Subject
import werewolf.model.entities.Player
import werewolf.model.entities.werewolves.Werewolf
import werewolf.view.GameUiEvent
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersSignal

class VeteranFragment(
    onActionSubject: Subject<GameUiEvent>,
    player: Player,
    targetPlayersOnActionSubject: Subject<TargetPlayersSignal>
) : PlayerGridFragment(onActionSubject,player,targetPlayersOnActionSubject){

    override fun initGridLayout(){
        gridLayout.addView(createTextView(MyApp.getAppContext().getString(R.string.alert)),gridLayout.childCount)
    }

    override fun setSelectedPlayer(playerName: String){
        selectedPlayer = Werewolf("Dummy Target")     //Dummy target
    }

}