package werewolf.model.entities

import werewolf.view.TargetPlayersEnum

interface AbilityState{
    fun useAbility(player: AbstractPlayer)
    fun getAbilityState(): String
    fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum
}

open class Neutral: AbilityState{
    override fun getAbilityState(): String {
        return "Select player"
    }

    override fun useAbility(player: AbstractPlayer) {
        player.addUsedAbility()
    }

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return player.resolveFetchTargetPlayers()
    }
}

class NoAbility: Neutral() {
    override fun getAbilityState(): String {
        return "No Ability"
    }

    override fun useAbility(player: AbstractPlayer) {}

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return TargetPlayersEnum.SetNoTargetPlayers
    }
}

class OneTurnCooldown: Neutral(){
    override fun useAbility(player: AbstractPlayer){
        player.defineAbilityState(Neutral())
    }
    override fun getAbilityState(): String {
        return "Ability on cooldown"
    }

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return TargetPlayersEnum.SetNoTargetPlayers
    }
}

class NoUsesLeft: Neutral(){
    override fun getAbilityState(): String {
        return "No uses left"
    }

    override fun fetchTargetPlayers(player: AbstractPlayer): TargetPlayersEnum {
        return TargetPlayersEnum.SetNoTargetPlayers
    }
}