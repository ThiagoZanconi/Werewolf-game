package werewolf.view.howtoplay

import werewolf.model.Roles
import werewolf.view.R

object RoleImageProvider {
    fun getRoleImage(role: Roles): Int{
        val roleImageMap = mapOf(
            Roles.Werewolf to R.drawable.werewolf,
            Roles.EvilJailer to R.drawable.witch,
            Roles.Vampire to R.drawable.vampire,
            Roles.Necromancer to R.drawable.necromancer,
            Roles.Zombie to R.drawable.zombie,
            Roles.Arsonist to R.drawable.arsonist,
            Roles.Villager to R.drawable.villager,
            Roles.Protector to R.drawable.protector,
            Roles.Vigilante to R.drawable.vigilante,
            Roles.Reviver to R.drawable.reviver,
            Roles.Lightbearer to R.drawable.lightbearer,
            Roles.Jester to R.drawable.jester,
            Roles.Veteran to R.drawable.veteran,
            Roles.Elusive to R.drawable.elusive,
            Roles.Detonator to R.drawable.detonator,
            Roles.Stalker to R.drawable.stalker,
            Roles.Detective to R.drawable.detective,
            Roles.Disguiser to R.drawable.disguiser
        )

        return roleImageMap[role] ?: R.drawable.detonator
    }
}