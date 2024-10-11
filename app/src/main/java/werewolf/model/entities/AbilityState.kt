package werewolf.model.entities

interface AbilityState{
    fun setAbilityState(player: AbstractPlayer, abilityState: AbilityState)
    fun useAbility(player: AbstractPlayer): EndOfRoundAbility
    fun getAbilityState(): String
}

abstract class AbstractAbilityState: AbilityState{
    override fun getAbilityState(): String {
        return "Select player"
    }
}

class Neutral: AbstractAbilityState() {
    override fun setAbilityState(player: AbstractPlayer, abilityState: AbilityState) {
        player.defineAbilityState(abilityState)
    }

    override fun useAbility(player: AbstractPlayer):EndOfRoundAbility {
        return player.resolveAbility()
    }
}

class NoAbility: AbstractAbilityState() {
    override fun getAbilityState(): String {
        return "No Ability"
    }

    override fun setAbilityState(player: AbstractPlayer, abilityState: AbilityState) {

    }

    override fun useAbility(player: AbstractPlayer): EndOfRoundAbility {
        return NullAbility()
    }
}

class NotNullable: AbstractAbilityState(){
    override fun setAbilityState(player: AbstractPlayer, abilityState: AbilityState) {

    }

    override fun useAbility(player: AbstractPlayer):EndOfRoundAbility {
        return player.resolveAbility()
    }
}

class Cooldown: AbstractAbilityState(){
    override fun setAbilityState(player: AbstractPlayer, abilityState: AbilityState) {}

    override fun useAbility(player: AbstractPlayer): EndOfRoundAbility {
        player.cooldownTimer()
        return NullAbility()
    }

    override fun getAbilityState(): String {
        return "Ability on cooldown"
    }
}

class NoUsesLeft: AbstractAbilityState(){
    override fun setAbilityState(player: AbstractPlayer, abilityState: AbilityState) {

    }

    override fun useAbility(player: AbstractPlayer): EndOfRoundAbility {
        return NullAbility()
    }

    override fun getAbilityState(): String {
        return "No uses left"
    }
}