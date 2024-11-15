package werewolf.model.entities

data class Profile(
    val name: String,
    val jesterWins: Int,
    val werewolfWins: Int,
    val villagerWins: Int
)