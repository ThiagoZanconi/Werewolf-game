package werewolf.view

import werewolf.model.entities.Player

enum class TargetPlayersEnum{
    SetWerewolfTargets, SetAlivePlayersTarget, SetDeadTargets, SetOtherAlivePlayersTarget, SetWerewolfTeammates
}

class TargetPlayersSignal(private val targetPlayersEnum: TargetPlayersEnum){
    var targetPlayers: List<Player>? = null
    fun getTargetPlayersEnum(): TargetPlayersEnum {
        return targetPlayersEnum
    }
}