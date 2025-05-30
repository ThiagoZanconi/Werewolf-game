package werewolf.model.entities

interface AbilityState{
    fun useAbility(player: AbstractPlayer)
    fun getAbilityState(): String
}

abstract class AbstractAbilityState: AbilityState{
    override fun getAbilityState(): String {
        return "Select player"
    }

    override fun useAbility(player: AbstractPlayer) {
        player.addUsedAbility()
    }
}

class Neutral: AbstractAbilityState()

class NoAbility: AbstractAbilityState() {
    override fun getAbilityState(): String {
        return "No Ability"
    }
}

class OffCooldown: AbstractAbilityState(){

    override fun useAbility(player: AbstractPlayer) {
        player.addUsedAbility()
    }
}

class OneTurnCooldown: AbstractAbilityState(){
    override fun useAbility(player: AbstractPlayer){
        player.defineAbilityState(OffCooldown())
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