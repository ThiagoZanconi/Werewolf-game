package werewolf.view

enum class InitUiEvent{
    AddLocalPlayer, AddConnectedPlayer, RemovePlayer, StartGame
}

class InitUiSignal(private val initUiEvent: InitUiEvent, private val playerName: String = ""){
    fun getEvent(): InitUiEvent{
        return initUiEvent
    }

    fun getPlayerName(): String{
        return playerName
    }
}