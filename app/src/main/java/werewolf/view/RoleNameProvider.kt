package werewolf.view

import werewolf.model.Roles

object RoleNameProvider {
    fun getRoleName(role: Roles): String{
        val roleMap = mapOf(
            Roles.Werewolf to R.string.werewolf,
            Roles.Witch to R.string.witch,
            Roles.Vampire to R.string.vampire,
            Roles.Necromancer to R.string.necromancer,
            Roles.Zombie to R.string.zombie,
            Roles.Arsonist to R.string.arsonist,
            Roles.Villager to R.string.villager,
            Roles.Protector to R.string.protector,
            Roles.Vigilante to R.string.vigilante,
            Roles.Priest to R.string.priest,
            Roles.Cleric to R.string.cleric,
            Roles.Veteran to R.string.veteran,
            Roles.Elusive to R.string.elusive,
            Roles.Jester to R.string.jester,
            Roles.Detonator to R.string.detonator,
            Roles.Stalker to R.string.stalker,
            Roles.Detective to R.string.detective
        )

        return MyApp.getAppContext().getString(roleMap[role] ?: R.string.unknown_role)
    }
}