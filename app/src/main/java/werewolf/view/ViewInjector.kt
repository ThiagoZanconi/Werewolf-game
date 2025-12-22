package werewolf.view

import kotlinx.coroutines.CoroutineScope
import werewolf.controller.ControllerInjector
import werewolf.model.ModelInjector
import werewolf.view.fragments.ServerFragment

object ViewInjector {

    fun initServer(fragment: ServerFragment) {
        ControllerInjector.onViewStarted(fragment)
    }

    fun initServerGameFragment(gameActivity: GameActivity, scope: CoroutineScope){
        ModelInjector.setServerGameView(gameActivity)
        ControllerInjector.onGameViewStarted(gameActivity, scope)
    }

}