package werewolf.model.entities

import werewolf.view.TargetPlayersEnum

enum class AbilityStateEnum{
    NEUTRAL, NOABILITY, ONCOOLDOWN, NOUSESLEFT
}

interface AbilityState{
    fun useAbility(player: AbstractPlayer)
    fun getAbilityState(): AbilityStateEnum
    fun turnSetUp(player: AbstractPlayer)
    fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum
}

open class Neutral: AbilityState{
    override fun getAbilityState(): AbilityStateEnum{
        return AbilityStateEnum.NEUTRAL
    }

    override fun useAbility(player: AbstractPlayer) {
        player.addUsedAbility()
    }

    override fun turnSetUp(player: AbstractPlayer){}

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return player.resolveFetchTargetPlayers()
    }
}

class NoAbility: Neutral() {
    override fun getAbilityState(): AbilityStateEnum{
        return AbilityStateEnum.NOABILITY
    }

    override fun useAbility(player: AbstractPlayer) {}

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return TargetPlayersEnum.SetNoTargetPlayers
    }
}

class OffCooldown: Neutral(){

    override fun useAbility(player: AbstractPlayer){}

    override fun getAbilityState(): AbilityStateEnum{
        return AbilityStateEnum.ONCOOLDOWN
    }

    override fun turnSetUp(player: AbstractPlayer){
        player.defineAbilityState(Neutral())
    }

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return TargetPlayersEnum.SetNoTargetPlayers
    }
}

class OneTurnCooldown: Neutral(){

    override fun useAbility(player: AbstractPlayer){}

    override fun getAbilityState(): AbilityStateEnum{
        return AbilityStateEnum.ONCOOLDOWN
    }

    override fun turnSetUp(player: AbstractPlayer){
        player.defineAbilityState(OffCooldown())
    }

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return TargetPlayersEnum.SetNoTargetPlayers
    }
}

class NoUsesLeft: Neutral(){
    override fun useAbility(player: AbstractPlayer){}

    override fun getAbilityState(): AbilityStateEnum{
        return AbilityStateEnum.NOUSESLEFT
    }

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return TargetPlayersEnum.SetNoTargetPlayers
    }
}