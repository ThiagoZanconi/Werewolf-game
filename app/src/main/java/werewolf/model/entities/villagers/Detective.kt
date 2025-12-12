package werewolf.model.entities.villagers

import org.json.JSONArray
import org.json.JSONObject
import werewolf.model.Roles
import werewolf.model.entities.AbstractAbility
import werewolf.model.entities.AbstractPlayer
import werewolf.model.entities.NoUsesLeft
import werewolf.model.entities.Player
import werewolf.model.entities.werewolves.Werewolf
import werewolf.view.FragmentEnum
import werewolf.view.MyApp
import werewolf.view.R

class Detective(
    playerName: String,
    private val werewolves: MutableList<Player>,
    private val villagers: MutableList<Player>,
): AbstractPlayer(playerName){
    private var investigatedPlayers: List<String> = listOf()

    override fun json(): JSONObject {
        val json = JSONObject()
        json.put("PlayerName", fetchPlayerName())
        json.put("AbilityState", fetchAbilityState())
        json.put("Role", fetchRole())
        json.put("ImageSrc", fetchImageSrc())
        json.put("Teammates", JSONArray(teammates))
        json.put("PossibleTargetPlayers", JSONArray(possibleTargetPlayers))
        json.put("Fragment", fetchFragment())
        json.put("InvestigatedPlayers", JSONArray(investigatedPlayers))

        return json
    }

    override fun fetchImageSrc(): Int {
        return R.drawable.detective
    }

    override fun fetchRole(): Roles {
        return Roles.Detective
    }

    override fun addUsedAbility() {
        investigatedPlayers = listOf()
        abilityState = NoUsesLeft()
        val werewolf = werewolves.random()
        usedAbilities.add(Investigate(this, werewolf,(werewolves + villagers - setOf(this,werewolf)).shuffled()))
    }

    override fun fetchFragment(): FragmentEnum {
        return if(investigatedPlayers.isEmpty()){
            FragmentEnum.DetectiveFragment
        } else{
            FragmentEnum.DetectiveGridFragment
        }
    }

    fun defineInvestigatedPlayers(investigatedPlayers: List<String>){
        this.investigatedPlayers = investigatedPlayers
    }

}

class Investigate(private val detective: Detective, private val werewolf: Player, private val playerList: List<Player>): AbstractAbility(Werewolf("Dummy Target")) {
    override fun resolve() {
        super.resolve()
        val investigatedPlayersList: MutableList<Player> =  mutableListOf(werewolf)
        investigatedPlayersList.add(playerList[0])
        investigatedPlayersList.add(playerList[1])
        detective.defineInvestigatedPlayers(investigatedPlayersList.map{it.fetchPlayerName()}.shuffled())
    }

    override fun fetchAbilityName(): String {
        return MyApp.getAppContext().getString(R.string.investigate)
    }

    override fun fetchPriority(): Int {
        return 3
    }
}