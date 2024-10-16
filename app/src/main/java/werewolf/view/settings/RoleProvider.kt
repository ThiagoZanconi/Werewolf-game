package werewolf.view.settings

import werewolf.model.Roles

object RoleProvider {
    fun roleList(): List<Roles>{
        return listOf(Roles.Witch,Roles.Vampire,Roles.Villager, Roles.Cleric, Roles.Vigilante, Roles.Priest, Roles.Protector)
    }
}