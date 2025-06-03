package werewolf.model.entities

interface PersistentEffect{
    fun fetchTargetPlayer(): Player
    fun setTargetPlayer(player: Player)
    fun resolve()

}

abstract class AbstractPersistentEffect(
    private var target: Player
): PersistentEffect{

    override fun fetchTargetPlayer(): Player {
        return target
    }

    override fun setTargetPlayer(player: Player){
        target = player
    }
}

class Bomb(target: Player): AbstractPersistentEffect(target){
    override fun resolve() {
        TODO("Not yet implemented")
    }
}