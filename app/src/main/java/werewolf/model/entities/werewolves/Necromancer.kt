package werewolf.model.entities.werewolves

import werewolf.model.Roles
import werewolf.model.entities.AbilityEventEnum
import werewolf.model.entities.AbilitySignal
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.Immune
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.TargetPlayersEnum
import werewolf.model.entities.WerewolfTeamPlayer
import werewolf.view.MyApp
import werewolf.view.R

class Necromancer(
    playerName: String
): WerewolfTeamPlayer(playerName){

    override fun fetchImageSrc(): Int {
        return R.drawable.necromancer
    }

    override fun fetchRole(): Roles {
        return Roles.Necromancer
    }

    override fun addUsedAbility(){
        abilityState = NoUsesLeft()
        usedAbilities.add(ZombieSpell(targetPlayers[0]))
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.DeadTargets
    }
}

class ZombieSpell(targetPlayer: Player): AbstractAbility(targetPlayer) {
    override fun resolve() {
        super.resolve()
        event = AbilityEventEnum.RevivePlayerZombie
        onActionSubject.notify(AbilitySignal(this))
        targetPlayer.defineDefenseState(Immune())
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.revive_spell)
    }

    override fun fetchPriority(): Int {
        return 1
    }
}