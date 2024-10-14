package werewolf.model.entities

interface AbilityState{
    fun useAbility(player: AbstractPlayer): Ability?
    fun getAbilityState(): String
}

abstract class AbstractAbilityState: AbilityState{
    override fun getAbilityState(): String {
        return "Select player"
    }

    override fun useAbility(player: AbstractPlayer): Ability? {
        return null
    }
}

class Neutral: AbstractAbilityState() {
    override fun useAbility(player: AbstractPlayer):Ability? {
        return player.resolveAbility()
    }
}

class NoAbility: AbstractAbilityState() {
    override fun getAbilityState(): String {
        return "No Ability"
    }
}

class OffCooldown: AbstractAbilityState(){

    override fun useAbility(player: AbstractPlayer):Ability? {
        return player.resolveAbility()
    }
}

class OneTurnCooldown: AbstractAbilityState(){
    override fun useAbility(player: AbstractPlayer): Ability? {
        player.defineAbilityState(OffCooldown())
        return null
    }
    override fun getAbilityState(): String {
        return "Ability on cooldown"
    }
}

class NoUsesLeft: AbstractAbilityState(){
    override fun getAbilityState(): String {
        return "No uses left"
    }
}