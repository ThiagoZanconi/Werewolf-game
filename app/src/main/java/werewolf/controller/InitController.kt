package werewolf.controller

import com.example.observer.Observer
import werewolf.model.GameSettings
import werewolf.model.GameSettingsImpl
import werewolf.view.InitActivity
import werewolf.view.InitUiEvent

interface InitController{
    fun setInitView(initActivity: InitActivity)
}

class InitControllerImpl: InitController {
    private lateinit var initActivity: InitActivity
    private val gameSettings: GameSettings = GameSettingsImpl

    override fun setInitView(initActivity: InitActivity) {
        this.initActivity = initActivity
        initActivity.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<InitUiEvent> =
        Observer { value ->
            when (value) {
                InitUiEvent.AddPlayer -> addPlayer()
                InitUiEvent.RemovePlayer -> removePlayer()
                InitUiEvent.StartGame -> startGame()
            }
        }

    private fun addPlayer() {
        val name = initActivity.getEditTextName()
        val added = gameSettings.addPlayer(name)
        if(added){
            initActivity.addPlayer(name)
        }
    }

    private fun removePlayer() {
        val index = obtainIndex()
        val name = gameSettings.removePlayer(index)
        initActivity.removePlayer(index,name)
    }

    private fun startGame() {
        if(gameSettings.fetchPlayers().isNotEmpty()){
            gameSettings.init()
            initActivity.startGame()
        }
    }

    private fun obtainIndex(): Int {
        return gameSettings.fetchPlayers().indexOfFirst { it == initActivity.getPlayerToRemove() }
    }
}