package werewolf.model.entities

enum class PlayerEventEnum{
    KilledPlayer, JesterWin, WerewolfKilled
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