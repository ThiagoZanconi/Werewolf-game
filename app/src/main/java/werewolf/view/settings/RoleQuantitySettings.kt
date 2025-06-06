package werewolf.view.settings

import werewolf.model.Roles

interface RoleQuantitySettings{
    fun init(playerSize: Int)
    fun size(): Int
    fun indexOf(index: Int): Roles
    fun fetchRoleMaxQuantity(role: Roles): Int
    fun fetchRoleQuantity(role: Roles): Int
    fun setRoleQuantity(role: Roles, size: Int)
    fun subtractRoleQuantity(role: Roles)
}

class RoleQuantitySettingsImpl: RoleQuantitySettings{
    private val rolesQuantityMap: MutableMap<Roles, Int> = mutableMapOf()
    private val rolesMaxQuantityMap: MutableMap<Roles, Int> = mutableMapOf()

    override fun init(playerSize: Int) {
        Roles.entries.forEach{
            rolesMaxQuantityMap[it] = playerSize - playerSize/3
        }
        rolesMaxQuantityMap.remove(Roles.Werewolf)
        rolesMaxQuantityMap.remove(Roles.Zombie)
        rolesMaxQuantityMap[Roles.Jester] = 1
        rolesMaxQuantityMap[Roles.Necromancer] = playerSize/3 -1
        rolesMaxQuantityMap[Roles.Vampire] = playerSize/3 -1
        rolesMaxQuantityMap[Roles.Witch] = playerSize/3 -1
        rolesMaxQuantityMap[Roles.Arsonist] = playerSize/3 -1
        if(playerSize<6){
            rolesMaxQuantityMap[Roles.Disguiser] = 0
        }
        rolesMaxQuantityMap.keys.forEach{
            rolesQuantityMap[it] = rolesMaxQuantityMap[it]!!
        }
    }

    override fun size(): Int {
        return rolesMaxQuantityMap.size
    }

    override fun indexOf(index: Int): Roles {
        return rolesMaxQuantityMap.keys.elementAt(index)
    }

    override fun fetchRoleMaxQuantity(role: Roles):Int {
        return rolesMaxQuantityMap[role]!!
    }

    override fun fetchRoleQuantity(role:Roles): Int{
        return rolesQuantityMap[role]!!
    }

    override fun setRoleQuantity(role: Roles, size: Int){
        rolesQuantityMap[role] = size
    }

    override fun subtractRoleQuantity(role: Roles) {
        rolesQuantityMap[role] = rolesQuantityMap[role]!!-1
    }
}