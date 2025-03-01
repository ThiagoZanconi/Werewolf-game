package werewolf.view

import werewolf.model.Roles

object RoleNameProvider {
    fun getRoleName(role: Roles): String{
        val roleMap = mapOf(
            Roles.Werewolf to R.string.werewolf,
            Roles.Witch to R.string.witch,
            Roles.Vampire to R.string.vampire,
            Roles.Villager to R.string.villager,
            Roles.Protector to R.string.protector,
            Roles.Vigilante to R.string.vigilante,
            Roles.Priest to R.string.priest,
            Roles.Cleric to R.string.cleric,
            Roles.Veteran to R.string.veteran,
            Roles.Jester to R.string.jester
        )

        return MyApp.getAppContext().getString(roleMap[role] ?: R.string.unknown_role)
    }
}