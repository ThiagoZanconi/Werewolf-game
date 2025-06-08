package werewolf.model.entities.villagers

import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.DeathCause
import werewolf.model.entities.Player
import werewolf.model.entities.VillagerAttackAbility
import werewolf.view.MyApp
import werewolf.view.R
import werewolf.view.TargetPlayersEnum

class Detonator(
    playerName: String
): AbstractPlayer(playerName){
    private var bomb: Bomb? = null

    override fun fetchImageSrc(): Int {
        return R.drawable.detonator
    }

    override fun fetchRole(): Roles {
        return Roles.Detonator
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.OtherPlayersTarget
    }

    override fun addUsedAbility() {
        usedAbilities.add(PlaceBomb(targetPlayers[0],this))
        if(bomb!=null){
            usedAbilities.add(RemoveBomb(bomb!!.fetchTargetPlayer(),this))
        }
    }

    override fun applyDamage(deathCause: DeathCause) {
        if(usedAbilities.isNotEmpty()){
            usedAbilities[0].cancel()
        }
        if(bomb!=null){
            bomb!!.fetchTargetPlayer().receiveAttack(bomb!!)
        }
        notifyKilledPlayer(deathCause)
    }

    fun setBombTarget(targetPlayer: Player): Bomb{
        bomb = Bomb(targetPlayer)
        return bomb!!
    }

    fun removeBomb(){
        bomb = null
    }
}

class PlaceBomb(targetPlayer: Player, private val detonator: Detonator): AbstractAbility(targetPlayer) {
    override fun resolve() {
        super.resolve()
        val bomb = detonator.setBombTarget(targetPlayer)
        targetPlayer.addPersistentAbility(bomb)
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
        detonator.removeBomb()
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.remove_bomb)
    }

    override fun fetchPriority(): Int {
        return 3
    }
}

class Bomb(target: Player): VillagerAttackAbility(target){
    override val deathCause: DeathCause = DeathCause.EXPLODED

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.place_bomb)
    }

    override fun fetchPriority(): Int {
        return 3
    }
}