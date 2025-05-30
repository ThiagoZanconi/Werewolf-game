package werewolf.model.entities

enum class PlayerEventEnum{
    SetWerewolfTargets, KilledPlayer, SetAlivePlayersTarget, JesterWin,
    WerewolfKilled, SetDeadTargets, SetOtherAlivePlayersTarget,
    SetWerewolfTeammates
}

enum class AbilityEventEnum{
    CancelAbility, RevivePlayer, RevivePlayerZombie
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