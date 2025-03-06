package werewolf.view.settings

import werewolf.model.Roles

interface RoleQuantitySettings{
    fun init(playerSize: Int)
    fun setRoleQuantity(role: Roles, size: Int)
    fun getRoleQuantity(role:Roles): Int
    fun roleRestrictionSubtraction(role: Roles)
}

class RoleQuantitySettingsImpl: RoleQuantitySettings{
    private val rolesQuantityMap: MutableMap<Roles, Int> = mutableMapOf()

    override fun init(playerSize: Int) {
        Roles.entries.forEach{
            rolesQuantityMap[it] = playerSize
        }
    }

    override fun setRoleQuantity(role: Roles, size: Int){
        rolesQuantityMap[role] = size
    }

    override fun getRoleQuantity(role:Roles): Int{
        return rolesQuantityMap[role]!!
    }

    override fun roleRestrictionSubtraction(role: Roles) {
        rolesQuantityMap[role] = rolesQuantityMap[role]!!-1
    }
}