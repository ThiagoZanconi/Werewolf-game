package werewolf.controller

import werewolf.model.ModelInjector
import werewolf.view.GameActivity
import werewolf.view.InitActivity

object ControllerInjector {

    fun onViewStarted(initActivity: InitActivity) {
        InitControllerImpl(ModelInjector.getModel()).apply {
            setInitView(initActivity)
        }
    }

    fun onGameViewStarted(gameActivity: GameActivity){
        GameControllerImpl(ModelInjector.getGameStateModel(),ModelInjector.getModel().players()).apply {
            setGameView(gameActivity)
        }
    }
}