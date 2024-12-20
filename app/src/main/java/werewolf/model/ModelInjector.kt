package werewolf.model

import android.content.Context
import werewolf.view.GameActivity
import werewolf.view.InitActivity

object ModelInjector {
    private lateinit var initModel: InitModel
    private lateinit var gameStateModel: GameStateModel

    fun initModel(initActivity: InitActivity) {
        initModel = InitModelImpl()
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