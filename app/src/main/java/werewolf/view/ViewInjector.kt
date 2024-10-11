package werewolf.view

import werewolf.controller.ControllerInjector
import werewolf.model.ModelInjector

object ViewInjector {

    fun init(initActivity: InitActivity) {
        ModelInjector.initModel(initActivity)
        ControllerInjector.onViewStarted(initActivity)
    }

    fun initGameActivity(gameActivity: GameActivity){
        ModelInjector.setGameView(gameActivity)
        ControllerInjector.onGameViewStarted(gameActivity)
    }
}