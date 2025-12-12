package werewolf.controller

import com.example.observer.Observer
import werewolf.model.GameSettings
import werewolf.model.GameSettingsImpl
import werewolf.view.InitUiEvent
import werewolf.view.InitUiSignal
import werewolf.view.fragments.ServerFragment

interface InitController{
    fun setInitView(playerSelectionFragment: ServerFragment)
}

class InitControllerImpl: InitController {
    private lateinit var playerSelectionFragment: ServerFragment
    private val gameSettings: GameSettings = GameSettingsImpl

    override fun setInitView(playerSelectionFragment: ServerFragment) {
        this.playerSelectionFragment = playerSelectionFragment
        playerSelectionFragment.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<InitUiSignal> =
        Observer { value ->
            when (value.getEvent()) {
                InitUiEvent.AddLocalPlayer -> addLocalPlayer(value.getPlayerName())
                InitUiEvent.AddConnectedPlayer -> addConnectedPlayer(value.getPlayerName())
                InitUiEvent.RemovePlayer -> removePlayer(value.getPlayerName())
                InitUiEvent.StartGame -> startGame()
            }
        }

    private fun addLocalPlayer(playerName: String) {
        val added = gameSettings.addLocalPlayer(playerName)
        if(added){
            playerSelectionFragment.addLocalPlayer(playerName)
        }
    }

    private fun addConnectedPlayer(playerName: String){
        println("Players: ${gameSettings.fetchPlayers()}")
        val added = gameSettings.addPlayer(playerName)
        if(added){
            playerSelectionFragment.addConnectedPlayer(playerName)
        }
    }

    private fun removePlayer(playerName: String) {
        val index = obtainIndex(playerName)
        val name = gameSettings.removePlayer(index)
        playerSelectionFragment.removePlayer(index,name)
    }

    private fun startGame() {
        if(gameSettings.fetchPlayers().size>=3){
            gameSettings.init()
            playerSelectionFragment.startGame()
        }
    }

    private fun obtainIndex(name: String): Int {
        return gameSettings.fetchPlayers().indexOfFirst { it == name }
    }
}