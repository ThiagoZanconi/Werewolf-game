package werewolf.model.entities

import androidx.fragment.app.Fragment
import com.example.observer.Subject
import werewolf.view.GameUiEvent
import werewolf.view.TargetPlayersEnum
import werewolf.view.TargetPlayersSignal
import werewolf.view.fragments.PlayerFragment

enum class AbilityStateEnum{
    NEUTRAL, NOABILITY, ONCOOLDOWN, NOUSESLEFT
}

interface AbilityState{
    fun useAbility(player: AbstractPlayer)
    fun getAbilityState(): AbilityStateEnum
    fun turnSetUp(player: AbstractPlayer)
    fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum
    fun fetchView(player: AbstractPlayer, onActionSubject: Subject<GameUiEvent>, targetPlayersOnActionSubject: Subject<TargetPlayersSignal>): Fragment
}

open class Neutral: AbilityState{
    override fun getAbilityState(): AbilityStateEnum{
        return AbilityStateEnum.NEUTRAL
    }

    override fun useAbility(player: AbstractPlayer) {
        player.addUsedAbility()
    }

    override fun turnSetUp(player: AbstractPlayer){}

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return player.resolveFetchTargetPlayers()
    }

    override fun fetchView(player: AbstractPlayer, onActionSubject: Subject<GameUiEvent>, targetPlayersOnActionSubject: Subject<TargetPlayersSignal>): Fragment {
        return player.resolveFetchView(onActionSubject,targetPlayersOnActionSubject)
    }
}

class NoAbility: Neutral() {
    override fun getAbilityState(): AbilityStateEnum{
        return AbilityStateEnum.NOABILITY
    }

    override fun useAbility(player: AbstractPlayer) {}

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return TargetPlayersEnum.NoTargetPlayers
    }

    override fun fetchView(player: AbstractPlayer, onActionSubject: Subject<GameUiEvent>, targetPlayersOnActionSubject: Subject<TargetPlayersSignal>): Fragment {
        return PlayerFragment(onActionSubject,player)
    }
}

class OffCooldown: Neutral(){

    override fun useAbility(player: AbstractPlayer){}

    override fun getAbilityState(): AbilityStateEnum{
        return AbilityStateEnum.ONCOOLDOWN
    }

    override fun turnSetUp(player: AbstractPlayer){
        player.defineAbilityState(Neutral())
    }

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return TargetPlayersEnum.NoTargetPlayers
    }

    override fun fetchView(player: AbstractPlayer, onActionSubject: Subject<GameUiEvent>, targetPlayersOnActionSubject: Subject<TargetPlayersSignal>): Fragment {
        return PlayerFragment(onActionSubject,player)
    }
}

class OneTurnCooldown: Neutral(){

    override fun useAbility(player: AbstractPlayer){}

    override fun getAbilityState(): AbilityStateEnum{
        return AbilityStateEnum.ONCOOLDOWN
    }

    override fun turnSetUp(player: AbstractPlayer){
        player.defineAbilityState(OffCooldown())
    }

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return TargetPlayersEnum.NoTargetPlayers
    }

    override fun fetchView(player: AbstractPlayer, onActionSubject: Subject<GameUiEvent>, targetPlayersOnActionSubject: Subject<TargetPlayersSignal>): Fragment {
        return PlayerFragment(onActionSubject,player)
    }
}

class NoUsesLeft: Neutral(){
    override fun useAbility(player: AbstractPlayer){}

    override fun getAbilityState(): AbilityStateEnum{
        return AbilityStateEnum.NOUSESLEFT
    }

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return TargetPlayersEnum.NoTargetPlayers
    }

    override fun fetchView(player: AbstractPlayer, onActionSubject: Subject<GameUiEvent>, targetPlayersOnActionSubject: Subject<TargetPlayersSignal>): Fragment {
        return PlayerFragment(onActionSubject,player)
    }
}