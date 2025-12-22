package werewolf.controller

import kotlinx.coroutines.CoroutineScope
import werewolf.model.ModelInjector
import werewolf.view.GameActivity
import werewolf.view.fragments.ServerFragment

object ControllerInjector {

    fun onViewStarted(playerSelectionFragment: ServerFragment) {
        InitControllerImpl().apply {
            setInitView(playerSelectionFragment)
        }
    }

    fun onGameViewStarted(gameActivity: GameActivity, scope: CoroutineScope){
        GameControllerImpl(ModelInjector.getGameStateModel()).apply {
            setGameView(gameActivity, scope)
        }
    }
}