package werewolf.view.settings

import werewolf.model.Roles
import werewolf.view.MyApp
import werewolf.view.R

object RoleNameProvider {
    fun getRoleName(role: Roles): String{
        return when(role){
            Roles.Werewolf -> MyApp.getAppContext().getString(R.string.witch)
            Roles.Witch -> MyApp.getAppContext().getString(R.string.witch)
            Roles.Vampire -> MyApp.getAppContext().getString(R.string.vampire)
            Roles.Villager -> MyApp.getAppContext().getString(R.string.villager)
            Roles.Protector -> MyApp.getAppContext().getString(R.string.protector)
            Roles.Vigilante -> MyApp.getAppContext().getString(R.string.vigilante)
            Roles.Priest -> MyApp.getAppContext().getString(R.string.priest)
            Roles.Cleric -> MyApp.getAppContext().getString(R.string.cleric)
            Roles.Jester -> MyApp.getAppContext().getString(R.string.jester)
        }

    }
}