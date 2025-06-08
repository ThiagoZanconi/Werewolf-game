package werewolf.view

import werewolf.model.entities.Player

enum class TargetPlayersEnum{
    NoTargetPlayers, WerewolfTargets, AlivePlayersTarget, DeadTargets, WerewolfTeammates, OtherPlayersTarget
}

class TargetPlayersSignal(private val targetPlayersEnum: TargetPlayersEnum){
    var targetPlayers: List<Player> = listOf()
    fun getTargetPlayersEnum(): TargetPlayersEnum {
        return targetPlayersEnum
    }
}