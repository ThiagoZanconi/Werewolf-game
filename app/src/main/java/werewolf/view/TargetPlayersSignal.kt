package werewolf.view

import werewolf.model.entities.Player

enum class TargetPlayersEnum{
    SetNoTargetPlayers, SetWerewolfTargets, SetAlivePlayersTarget, SetDeadTargets, SetOtherAlivePlayersTarget, SetWerewolfTeammates
}

class TargetPlayersSignal(private val targetPlayersEnum: TargetPlayersEnum){
    var targetPlayers: List<Player> = listOf()
    fun getTargetPlayersEnum(): TargetPlayersEnum {
        return targetPlayersEnum
    }
}