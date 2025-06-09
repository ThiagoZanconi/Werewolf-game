package werewolf.controller

import werewolf.model.ModelInjector
import werewolf.view.GameActivity
import werewolf.view.InitActivity

object ControllerInjector {

    fun onViewStarted(initActivity: InitActivity) {
        InitControllerImpl().apply {
            setInitView(initActivity)
        }
    }

    fun onGameViewStarted(gameActivity: GameActivity){
        GameControllerImpl(ModelInjector.getGameStateModel()).apply {
            setGameView(gameActivity)
        }
    }
}