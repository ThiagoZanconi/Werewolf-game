package werewolf.view

import werewolf.controller.ControllerInjector
import werewolf.model.ModelInjector
import java.io.File

object ViewInjector {

    fun init(initActivity: InitActivity) {
        ModelInjector.initModel(initActivity)
        ControllerInjector.onViewStarted(initActivity)
    }

    fun initGameActivity(gameActivity: GameActivity, settings:File){
        ModelInjector.setGameView(gameActivity,settings)
        ControllerInjector.onGameViewStarted(gameActivity)
    }
}