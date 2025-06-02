package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.Player
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersEnum

class Detonator(
    override val playerName: String
): AbstractPlayer(){
    override val role: Roles = Roles.Detonator
    private var bombTarget: Player? = null

    override fun fetchImageSrc(): Int {
        return R.drawable.detonator
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.SetAlivePlayersTarget
    }

    override fun addUsedAbility() {
        usedAbilities.add(PlaceBomb(targetPlayer!!,this))
        if(bombTarget!=null){
            usedAbilities.add(RemoveBomb(bombTarget!!,this))
        }
    }

    override fun applyDamage(deathCause: DeathCause) {
        println("Apply Damage")
        if(usedAbilities.isNotEmpty()){
            usedAbilities[0].cancel()
        }
        if(bombTarget!=null){
            println("Bomb Target")
            bombTarget!!.receiveDamage(DeathCause.EXPLODED)
        }
        notifyKilledPlayer(deathCause)
    }

    fun setBombTarget(targetPlayer: Player?){
        bombTarget = targetPlayer
    }
}

class PlaceBomb(targetPlayer: Player, private val detonator: Detonator): AbstractAbility(targetPlayer) {
    override fun resolve() {
        super.resolve()
        detonator.setBombTarget(targetPlayer)
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.place_bomb)
    }

    override fun fetchPriority(): Int {
        return 10
    }
}

class RemoveBomb(targetPlayer: Player, private val detonator: Detonator): AbstractAbility(targetPlayer) {
    override fun resolve() {
        super.resolve()
        detonator.setBombTarget(null)
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.remove_bomb)
    }

    override fun fetchPriority(): Int {
        return 3
    }
}