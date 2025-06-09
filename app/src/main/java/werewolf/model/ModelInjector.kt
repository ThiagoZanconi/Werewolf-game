package werewolf.model

import android.content.Context
import werewolf.view.GameActivity

object ModelInjector {
    private lateinit var gameStateModel: GameStateModel

    fun getGameStateModel(): GameStateModel{
        return gameStateModel
    }

    fun setGameView(gameActivity: GameActivity){
        gameStateModel = GameStateModelImpl(gameActivity as Context)
        gameStateModel.setGameView(gameActivity)
    }
}