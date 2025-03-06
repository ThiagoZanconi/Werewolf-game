package werewolf.view

import werewolf.controller.ControllerInjector
import werewolf.model.ModelInjector
import werewolf.view.settings.RoleQuantitySettings

object ViewInjector {

    fun init(initActivity: InitActivity,roleQuantitySettings: RoleQuantitySettings) {
        ModelInjector.initModel(initActivity, roleQuantitySettings)
        ControllerInjector.onViewStarted(initActivity)
    }

    fun initGameActivity(gameActivity: GameActivity){
        ModelInjector.setGameView(gameActivity)
        ControllerInjector.onGameViewStarted(gameActivity)
    }
}