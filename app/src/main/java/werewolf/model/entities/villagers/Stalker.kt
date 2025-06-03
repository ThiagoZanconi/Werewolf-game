package werewolf.model.entities.villagers

import androidx.fragment.app.Fragment
import com.example.observer.Subject
import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.view.GameUiEvent
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersEnum
import werewolf.view.TargetPlayersSignal
import werewolf.view.fragments.StalkerFragment

class Stalker(
    override val playerName: String
): AbstractPlayer(){
    override val role: Roles = Roles.Stalker
    private var stalkedPair: Pair<Player,String>? = null

    override fun addUsedAbility() {
        abilityState = NoUsesLeft()
        usedAbilities.add(Stalk(targetPlayers[0],this))
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.stalker
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.SetAlivePlayersTarget
    }

    override fun fetchView(onActionSubject: Subject<GameUiEvent>, targetPlayersOnActionSubject: Subject<TargetPlayersSignal>): Fragment {
        return StalkerFragment(onActionSubject,this, targetPlayersOnActionSubject)
    }

    fun setStalkedPlayer(playerName: String){
        stalkedPair = Pair(targetPlayers[0],playerName)
    }

    fun fetchStalkedPair(): Pair<Player,String>?{
        return stalkedPair
    }

}

class Stalk(targetPlayer: Player, private val stalker: Stalker): AbstractAbility(targetPlayer) {
    override fun resolve() {
        super.resolve()
        val stalkedPlayers =( targetPlayer.fetchTargetPlayers() + targetPlayer.fetchVisitors() ).shuffled()
        stalker.setStalkedPlayer(stalkedPlayers.firstOrNull()?.fetchPlayerName() ?: "")
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.stalk)
    }

    override fun fetchPriority(): Int {
        return 15
    }
}