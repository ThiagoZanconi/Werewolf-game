package werewolf.view

import werewolf.model.entities.AbilityStateEnum

object AbilityStateProvider {
    fun getAbilityState(abilityState: AbilityStateEnum): String{
        val roleMap = mapOf(
            AbilityStateEnum.NOUSESLEFT to R.string.no_uses_left,
            AbilityStateEnum.NEUTRAL to R.string.select_player,
            AbilityStateEnum.NOABILITY to R.string.no_ability,
            AbilityStateEnum.ONCOOLDOWN to R.string.on_cooldown
        )

        return MyApp.getAppContext().getString(roleMap[abilityState] ?: R.string.unknown_ability_state)
    }
}