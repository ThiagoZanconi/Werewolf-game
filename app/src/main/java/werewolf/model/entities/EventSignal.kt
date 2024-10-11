package werewolf.model.entities

enum class PlayerEventEnum{
    SetWerewolfTargets, KilledPlayer, SetAlivePlayersTarget, JesterWin, WerewolfKilled, SetDeadTargets, RevivedPlayer, SetNoTargets
}

enum class AbilityEventEnum{
    CancelAbility
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