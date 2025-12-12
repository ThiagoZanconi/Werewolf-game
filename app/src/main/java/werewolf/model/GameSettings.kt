package werewolf.model

interface GameSettings{
    fun init()
    fun reset()
    fun size(): Int
    fun fetchPlayers(): MutableList<String>
    fun fetchRoleMaxQuantity(role: Roles): Int
    fun fetchRoleQuantity(role: Roles): Int
    fun addPlayer(player: String): Boolean
    fun addLocalPlayer(player: String): Boolean
    fun removePlayer(index: Int): String
    fun fetchLocalPlayersAndRemoveClientPlayers(): List<String>
    fun indexOfRole(index: Int): Roles
    fun setRoleQuantity(role: Roles, size: Int)
    fun subtractRoleQuantity(role: Roles)
}

object GameSettingsImpl: GameSettings {
    private val rolesQuantityMap: MutableMap<Roles, Int> = mutableMapOf()
    private val rolesMaxQuantityMap: MutableMap<Roles, Int> = mutableMapOf()
    private val players: MutableList<String> = mutableListOf()
    private val localPlayers: MutableList<String> = mutableListOf()

    override fun init() {
        Roles.entries.forEach{
            rolesMaxQuantityMap[it] = players.size - players.size/3
        }
        rolesMaxQuantityMap.remove(Roles.Werewolf)
        rolesMaxQuantityMap.remove(Roles.Zombie)
        rolesMaxQuantityMap[Roles.Jester] = 1
        rolesMaxQuantityMap[Roles.Necromancer] = players.size/3 -1
        rolesMaxQuantityMap[Roles.Vampire] = players.size/3 -1
        rolesMaxQuantityMap[Roles.Witch] = players.size/3 -1
        rolesMaxQuantityMap[Roles.Arsonist] = players.size/3 -1
        if(players.size<6){
            rolesMaxQuantityMap[Roles.Disguiser] = 0
            rolesMaxQuantityMap[Roles.Detective] = 0
        }
        rolesMaxQuantityMap.keys.forEach{
            rolesQuantityMap[it] = rolesMaxQuantityMap[it]!!
        }
    }

    override fun reset() {
        rolesQuantityMap.clear()
        rolesMaxQuantityMap.clear()
        players.clear()
        localPlayers.clear()
    }

    override fun size(): Int {
        return rolesMaxQuantityMap.size
    }

    override fun indexOfRole(index: Int): Roles {
        return rolesMaxQuantityMap.keys.elementAt(index)
    }

    override fun fetchRoleMaxQuantity(role: Roles):Int {
        return rolesMaxQuantityMap[role]!!
    }

    override fun fetchRoleQuantity(role:Roles): Int{
        return rolesQuantityMap[role]!!
    }

    override fun fetchPlayers(): MutableList<String> {
        return players
    }

    override fun addPlayer(player: String): Boolean {
        return if(player !in players && player!=""){
            players.add(player)
            true
        }
        else{
            false
        }
    }

    override fun addLocalPlayer(player: String): Boolean {
        val added = addPlayer(player)
        if(added){
            localPlayers.add(player)
        }
        return added
    }

    override fun removePlayer(index: Int): String {
        val playerRemoved = players.removeAt(index)
        localPlayers.remove(playerRemoved)
        return playerRemoved
    }

    override fun fetchLocalPlayersAndRemoveClientPlayers(): List<String> {
        players.clear()
        localPlayers.forEach {
            players.add(it)
        }
        return localPlayers
    }

    override fun setRoleQuantity(role: Roles, size: Int){
        rolesQuantityMap[role] = size
    }

    override fun subtractRoleQuantity(role: Roles) {
        rolesQuantityMap[role] = rolesQuantityMap[role]!!-1
    }
}