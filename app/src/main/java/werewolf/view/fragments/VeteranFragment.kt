package werewolf.view.fragments

import com.example.observer.Subject
import werewolf.model.entities.Player
import werewolf.model.entities.werewolves.Werewolf
import werewolf.view.GameUiEvent
import werewolf.view.MyApp
import werewolf.view.R

class VeteranFragment(
    onActionSubject: Subject<GameUiEvent>,
    player: Player
) : PlayerGridFragment(onActionSubject,player){

    override fun initGridLayout(){
        gridLayout.addView(createTextView(MyApp.getAppContext().getString(R.string.alert)),gridLayout.childCount)
    }

    override fun setSelectedPlayer(playerName: String){
        selectedPlayer = Werewolf("Dummy Target")     //Dummy target
    }

}