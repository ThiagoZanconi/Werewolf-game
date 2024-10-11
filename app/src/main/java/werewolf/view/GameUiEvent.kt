package werewolf.view

sealed class GameUiEvent {
    data object ConfirmAction : GameUiEvent()
    data object StartTurn: GameUiEvent()
    data object StartNextRound: GameUiEvent()
}