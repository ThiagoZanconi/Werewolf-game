package werewolf.view.settings

import werewolf.model.Roles

interface RoleQuantitySettings{
    fun init(playerSize: Int)
    fun getRoleMaxQuantity(role: Roles): Int
    fun setRoleQuantity(role: Roles, size: Int)
    fun getRoleQuantity(role:Roles): Int
    fun subtractRoleQuantity(role: Roles)
}

class RoleQuantitySettingsImpl: RoleQuantitySettings{
    private val rolesQuantityMap: MutableMap<Roles, Int> = mutableMapOf()
    private val rolesMaxQuantityMap: MutableMap<Roles, Int> = mutableMapOf()

    override fun init(playerSize: Int) {
        Roles.entries.forEach{
            rolesMaxQuantityMap[it] = playerSize - playerSize/3
        }
        rolesMaxQuantityMap[Roles.Jester] = 1
        rolesMaxQuantityMap[Roles.Necromancer] = playerSize/3 -1
        rolesMaxQuantityMap[Roles.Vampire] = playerSize/3 -1
        rolesMaxQuantityMap[Roles.Witch] = playerSize/3 -1
        rolesMaxQuantityMap.keys.forEach{
            rolesQuantityMap[it] = rolesMaxQuantityMap[it]!!
        }
    }

    override fun getRoleMaxQuantity(role: Roles):Int {
        return rolesMaxQuantityMap[role]!!
    }

    override fun setRoleQuantity(role: Roles, size: Int){
        rolesQuantityMap[role] = size
    }

    override fun getRoleQuantity(role:Roles): Int{
        return rolesQuantityMap[role]!!
    }

    override fun subtractRoleQuantity(role: Roles) {
        rolesQuantityMap[role] = rolesQuantityMap[role]!!-1
    }
}