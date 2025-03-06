package werewolf.model

import android.content.Context
import werewolf.view.GameActivity
import werewolf.view.InitActivity
import werewolf.view.settings.RoleQuantitySettings

object ModelInjector {
    private lateinit var initModel: InitModel
    private lateinit var gameStateModel: GameStateModel

    fun initModel(initActivity: InitActivity,roleQuantitySettings: RoleQuantitySettings) {
        initModel = InitModelImpl(roleQuantitySettings)
        initModel.setInitView(initActivity)
    }

    fun getModel(): InitModel{
        return initModel
    }

    fun getGameStateModel(): GameStateModel{
        return gameStateModel
    }

    fun setGameView(gameActivity: GameActivity){
        gameStateModel = GameStateModelImpl(initModel.getRolesQuantityRestrictions(),gameActivity as Context)
        gameStateModel.setGameView(gameActivity)
    }
}