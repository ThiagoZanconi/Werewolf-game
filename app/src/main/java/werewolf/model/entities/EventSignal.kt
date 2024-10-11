package werewolf.model.entities

enum class PlayerEventEnum{
    SetWerewolfTargets, KilledPlayer, SetAlivePlayersTarget, JesterWin, WerewolfKilled, SetDeadTargets, RevivedPlayer, SetNoTargets
}

class EventSignal(private val player: Player){
    fun getPlayer(): Player{
        return player
    }
}