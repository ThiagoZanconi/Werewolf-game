package werewolf.view.howtoplay

import werewolf.model.Roles
import werewolf.view.MyApp
import werewolf.view.R

object RoleDescriptionProvider {
    fun getRoleDescription(role: Roles): String{
        val roleDescriptionMap = mapOf(
            Roles.Werewolf to R.string.werewolf_description,
            Roles.Witch to R.string.witch_description,
            Roles.Vampire to R.string.vampire_description,
            Roles.Villager to R.string.villager_description,
            Roles.Protector to R.string.protector_description,
            Roles.Vigilante to R.string.vigilante_description,
            Roles.Priest to R.string.priest_description,
            Roles.Cleric to R.string.cleric_description,
            Roles.Jester to R.string.jester_description
        )

        return MyApp.getAppContext().getString(roleDescriptionMap[role] ?: R.string.unknown_description)
    }
}