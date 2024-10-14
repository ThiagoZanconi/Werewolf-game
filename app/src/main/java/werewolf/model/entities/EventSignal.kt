package werewolf.model.entities

enum class PlayerEventEnum{
    SetWerewolfTargets, KilledPlayer, SetAlivePlayersTarget, JesterWin, WerewolfKilled, SetDeadTargets, SetNoTargets, SetOtherAlivePlayersTarget
}

enum class AbilityEventEnum{
    CancelAbility, RevivePlayer
}

class PlayerSignal(private val player: Player){
    fun getPlayer(): Player{
        return player
    }
}

class AbilitySignal(private val ability: Ability){
    fun getAbility(): Ability{
        return ability
    }
}