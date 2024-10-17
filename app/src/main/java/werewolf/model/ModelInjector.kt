package werewolf.model

import werewolf.view.GameActivity
import werewolf.view.InitActivity
import java.io.File

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

    fun setGameView(gameActivity: GameActivity, settings: File){
        gameStateModel = GameStateModelImpl()
        gameStateModel.setGameView(gameActivity,settings)
    }

}