package werewolf.model

import werewolf.view.GameActivity

object ModelInjector {
    private lateinit var gameStateModel: GameStateModel

    fun getGameStateModel(): GameStateModel{
        return gameStateModel
    }

    fun setServerGameView(gameActivity: GameActivity){
        gameStateModel = GameStateModelImpl()
        gameStateModel.setGameView(gameActivity)
    }

}