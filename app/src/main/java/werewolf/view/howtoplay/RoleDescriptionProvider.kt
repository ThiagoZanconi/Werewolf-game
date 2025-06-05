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
            Roles.Necromancer to R.string.necromancer_description,
            Roles.Zombie to R.string.zombie_description,
            Roles.Arsonist to R.string.arsonist_description,
            Roles.Villager to R.string.villager_description,
            Roles.Protector to R.string.protector_description,
            Roles.Vigilante to R.string.vigilante_description,
            Roles.Priest to R.string.priest_description,
            Roles.Cleric to R.string.cleric_description,
            Roles.Jester to R.string.jester_description,
            Roles.Veteran to R.string.veteran_description,
            Roles.Elusive to R.string.elusive_description,
            Roles.Detonator to R.string.detonator_description,
            Roles.Stalker to R.string.stalker_description,
            Roles.Detective to R.string.detective_description
        )

        return MyApp.getAppContext().getString(roleDescriptionMap[role] ?: R.string.unknown_description)
    }
}