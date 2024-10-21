package werewolf.model

interface RolesQuantityRestrictions {
    fun addRoleRestriction(role: Roles, value: Int)
    fun getRoleRestriction(role: Roles): Int
    fun roleRestrictionSubtraction(role: Roles)

}
class RolesQuantityRestrictionsImpl(): RolesQuantityRestrictions{
    private val roleMapping: MutableMap<Roles, Int> = mutableMapOf()

    override fun addRoleRestriction(role: Roles, value: Int) {
        roleMapping[role] = value
    }

    override fun getRoleRestriction(role: Roles): Int {
        return roleMapping[role]!!
    }

    override fun roleRestrictionSubtraction(role: Roles) {
        roleMapping[role] = roleMapping[role]!!-1
    }
}