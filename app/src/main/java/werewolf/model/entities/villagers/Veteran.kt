package werewolf.model.entities.villagers

import androidx.fragment.app.Fragment
import com.example.observer.Subject
import werewolf.model.Roles
import werewolf.model.entities.Ability
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.werewolves.Werewolf
import werewolf.view.GameUiEvent
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.fragments.VeteranFragment

class Veteran(
    override val playerName: String
): AbstractPlayer(){
    override val role: Roles = Roles.Veteran

    override fun resolveAbility(): Ability? {
        abilityState = NoUsesLeft()
        usedAbility = Alert(visitors)
        return usedAbility
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.veteran
    }

    override fun fetchView(onActionSubject: Subject<GameUiEvent>): Fragment {
        return VeteranFragment(onActionSubject,this)
    }
}

class Alert(private val targetPlayers: MutableList<Player>): AbstractAbility(Werewolf("Dummy Target")){

    override fun resolve() {
        targetPlayers.forEach{
            it.receiveDamage(DeathCause.SHOT)
        }
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.alert)
    }

    override fun fetchPriority(): Int {
        return 2
    }
}