package werewolf.model.entities.villagers

import org.json.JSONArray
import org.json.JSONObject
import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.TargetPlayersEnum
import werewolf.view.FragmentEnum
import werewolf.view.MyApp
import werewolf.view.R

class Stalker(
    playerName: String
): AbstractPlayer(playerName){
    private var stalkedPair: String = ""

    override fun json(): JSONObject {
        val json = JSONObject()
        json.put("PlayerName", fetchPlayerName())
        json.put("AbilityState", fetchAbilityState())
        json.put("Role", fetchRole())
        json.put("ImageSrc", fetchImageSrc())
        json.put("Teammates", JSONArray(teammates))
        json.put("PossibleTargetPlayers", JSONArray(possibleTargetPlayers))
        json.put("Fragment", fetchFragment())
        json.put("StalkedPair", stalkedPair)

        return json
    }

    override fun addUsedAbility() {
        abilityState = NoUsesLeft()
        usedAbilities.add(Stalk(targetPlayers[0],this))
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.stalker
    }

    override fun fetchRole(): Roles {
        return Roles.Stalker
    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.AlivePlayersTarget
    }

    override fun fetchFragment(): FragmentEnum {
        return FragmentEnum.StalkerFragment
    }

    fun setStalkedPlayer(player: Player?){
        if(player!=null){
            stalkedPair = MyApp.getAppContext().getString(R.string.saw, targetPlayers[0].fetchPlayerName(), player.fetchPlayerName())
        }

    }

}

class Stalk(targetPlayer: Player, private val stalker: Stalker): AbstractAbility(targetPlayer) {
    override fun resolve() {
        super.resolve()
        val stalkedPlayers =( targetPlayer.fetchTargetPlayers() + targetPlayer.fetchVisitors() - stalker ).shuffled()
        stalker.setStalkedPlayer(stalkedPlayers.firstOrNull())
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.stalk)
    }

    override fun fetchPriority(): Int {
        return 15
    }
}