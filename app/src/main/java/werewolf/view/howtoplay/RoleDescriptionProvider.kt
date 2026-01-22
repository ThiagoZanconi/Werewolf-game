package werewolf.view.howtoplay

import werewolf.model.Roles
import werewolf.view.MyApp
import werewolf.view.R

object RoleDescriptionProvider {
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
}