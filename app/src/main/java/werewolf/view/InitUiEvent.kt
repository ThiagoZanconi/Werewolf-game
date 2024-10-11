package werewolf.view

sealed class InitUiEvent {
    data object AddPlayer : InitUiEvent()
    data object RemovePlayer : InitUiEvent()
    data object StartGame : InitUiEvent()
}