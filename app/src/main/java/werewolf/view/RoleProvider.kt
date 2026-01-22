package werewolf.view

import werewolf.model.Roles

object RoleProvider {
    fun getRoleName(role: Roles): String{
        val roleMap = mapOf(
            Roles.Werewolf to R.string.werewolf,
            Roles.EvilJailer to R.string.evil_jailer,
            Roles.Vampire to R.string.vampire,
            Roles.Skeleton to R.string.skeleton,
            Roles.Zombie to R.string.zombie,
            Roles.Arsonist to R.string.arsonist,
            Roles.Villager to R.string.villager,
            Roles.Protector to R.string.protector,
            Roles.Vigilante to R.string.vigilante,
            Roles.Reviver to R.string.reviver,
            Roles.Lightbearer to R.string.lightbearer,
            Roles.Veteran to R.string.veteran,
            Roles.Elusive to R.string.elusive,
            Roles.Jester to R.string.jester,
            Roles.Detonator to R.string.detonator,
            Roles.Stalker to R.string.stalker,
            Roles.Detective to R.string.detective,
            Roles.Disguiser to R.string.disguiser
        )

        return MyApp.getAppContext().getString(roleMap[role] ?: R.string.unknown_role)
    }

    fun getRoleDescription(role: Roles): String{
        val roleDescriptionMap = mapOf(
            Roles.Werewolf to R.string.werewolf_description,
            Roles.EvilJailer to R.string.evil_jailer_description,
            Roles.Vampire to R.string.vampire_description,
            Roles.Skeleton to R.string.skeleton_description,
            Roles.Zombie to R.string.zombie_description,
            Roles.Arsonist to R.string.arsonist_description,
            Roles.Villager to R.string.villager_description,
            Roles.Protector to R.string.protector_description,
            Roles.Vigilante to R.string.vigilante_description,
            Roles.Reviver to R.string.reviver_description,
            Roles.Lightbearer to R.string.lightbearer_description,
            Roles.Jester to R.string.jester_description,
            Roles.Veteran to R.string.veteran_description,
            Roles.Elusive to R.string.elusive_description,
            Roles.Detonator to R.string.detonator_description,
            Roles.Stalker to R.string.stalker_description,
            Roles.Detective to R.string.detective_description,
            Roles.Disguiser to R.string.disguiser_description
        )

        return MyApp.getAppContext().getString(roleDescriptionMap[role] ?: R.string.unknown_description)
    }

    fun getPremiumRoles(): List<Roles>{
        return listOf(Roles.Arsonist, Roles.Veteran, Roles.Elusive, Roles.Detonator, Roles.Detective)
    }
}