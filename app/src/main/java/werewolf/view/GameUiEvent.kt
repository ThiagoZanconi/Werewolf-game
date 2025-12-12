package werewolf.view

import org.json.JSONObject

enum class GameUiEvent{
    ConfirmAction, StartTurn, StartNextRound, AbilityUsed, HangedPlayer
}

class GameUiEventSignal(private val gameUiEvent: GameUiEvent, private val jsonObject: JSONObject){
    fun getEvent(): GameUiEvent{
        return gameUiEvent
    }

    fun getJson(): JSONObject{
        return jsonObject
    }
}