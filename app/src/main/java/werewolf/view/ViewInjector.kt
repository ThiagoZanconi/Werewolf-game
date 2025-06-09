package werewolf.view

import werewolf.controller.ControllerInjector
import werewolf.model.ModelInjector
import werewolf.model.GameSettings

object ViewInjector {

    fun init(initActivity: InitActivity) {
        ControllerInjector.onViewStarted(initActivity)
    }

    fun initGameActivity(gameActivity: GameActivity){
        ModelInjector.setGameView(gameActivity)
        ControllerInjector.onGameViewStarted(gameActivity)
    }
}