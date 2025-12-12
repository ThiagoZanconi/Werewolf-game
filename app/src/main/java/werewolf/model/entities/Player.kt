package werewolf.model.entities

import com.example.observer.Observable
import com.example.observer.Subject
import org.json.JSONArray
import org.json.JSONObject
import werewolf.model.Roles
import werewolf.view.FragmentEnum

enum class DeathCause{
    HANGED, MAULED, SHOT, EXPLODED
}

enum class TargetPlayersEnum{
    NoTargetPlayers, WerewolfTargets, AlivePlayersTarget, DeadTargets, WerewolfTeammates, OtherPlayersTarget
}

interface Player{
    val playerObservable: Observable<PlayerSignal>

    fun json(): JSONObject
    fun fetchPlayerName(): String
    fun fetchRole(): Roles
    fun fetchTeammates(): List<String>
    fun fetchTargetPlayersOptions(): TargetPlayersEnum
    fun fetchPossibleTargetPlayers(): List<String>
    fun fetchTargetPlayers(): List<Player>
    fun fetchEvent(): PlayerEventEnum
    fun fetchAbilityState(): AbilityStateEnum
    fun fetchAbilitiesUsedOnMe(): List<Ability>
    fun fetchUsedAbilities(): List<Ability>
    fun fetchVisitors(): List<Player>
    fun fetchPersistentAbilities(): MutableList<Ability>
    fun fetchUsedAbilityName(index: Int): String?
    fun fetchFragment(): FragmentEnum
    fun fetchDeathCause(): DeathCause
    fun fetchImageSrc(): Int

    fun defineTeammates(teammates: List<String>)
    fun definePossibleTargetPlayers(targetPlayers: List<String>)
    fun defineDefenseState(defenseState: DefenseState)
    fun defineTargetPlayer(targetPlayer: Player)
    fun definePersistentAbilities(abilities: MutableList<Ability>)

    fun addPersistentAbility(ability: Ability)

    fun removePersistentAbility(ability: Ability)

    //Ability delegated to abilityState contrary case
    fun notifyAbilityUsed(targetPlayers: List<Player>)
    fun notifyKilledPlayer(deathCause: DeathCause)

    fun resetVisitors()
    fun resetDefenseState()

    fun receiveAbility(ability: Ability)
    fun receiveAttack(villagerAttackAbility: VillagerAttackAbility)
    fun receiveAttack(werewolfAttackAbility: WerewolfAttackAbility)

    fun visitedBy(visitor: Player)
    fun turnSetUp()
    fun cancelAbility()
}

abstract class AbstractPlayer(private val playerName: String): Player{
    protected open var defenseState: DefenseState = NoDefense()
    protected open var abilityState: AbilityState = Neutral()
    protected open var usedAbilities: MutableList<Ability> = mutableListOf()
    protected lateinit var event: PlayerEventEnum
    protected lateinit var deathCause: DeathCause
    protected var visitors: MutableList<Player> = mutableListOf()
    protected var targetPlayers: MutableList<Player> = mutableListOf()
    protected var teammates: List<String> = listOf()
    private var abilitiesUsedOnMe: MutableList<Ability> = mutableListOf()
    private var persistentAbilities: MutableList<Ability> = mutableListOf()
    protected var possibleTargetPlayers: List<String> = listOf()
    protected val onActionSubject = Subject<PlayerSignal>()
    override val playerObservable: Observable<PlayerSignal> = onActionSubject

    override fun json(): JSONObject {
        val json = JSONObject()
        json.put("PlayerName", fetchPlayerName())
        json.put("AbilityState", fetchAbilityState())
        json.put("Role", fetchRole())
        json.put("ImageSrc", fetchImageSrc())
        json.put("Teammates", JSONArray(teammates))
        json.put("PossibleTargetPlayers", JSONArray(possibleTargetPlayers))
        json.put("Fragment", fetchFragment())
        return json
    }

    override fun fetchPlayerName(): String{
        return playerName
    }

    override fun fetchTeammates(): List<String> {
        return teammates
    }

    override fun fetchTargetPlayersOptions(): TargetPlayersEnum {
        return abilityState.fetchTargetPlayers(this)
    }

    override fun fetchPossibleTargetPlayers(): List<String> {
        return possibleTargetPlayers
    }

    override fun fetchTargetPlayers(): List<Player>{
        return targetPlayers
    }

    override fun fetchEvent(): PlayerEventEnum {
        return event
    }

    override fun fetchAbilityState(): AbilityStateEnum{
        return abilityState.getAbilityState()
    }
    override fun fetchAbilitiesUsedOnMe(): List<Ability>{
        return abilitiesUsedOnMe
    }

    override fun fetchUsedAbilities(): List<Ability>{
        return usedAbilities
    }

    override fun fetchUsedAbilityName(index: Int): String? {
        return if (usedAbilities.isNotEmpty())
            usedAbilities[index].fetchAbilityName()
        else
            null
    }

    override fun fetchVisitors(): List<Player>{
        return visitors
    }

    override fun fetchFragment(): FragmentEnum {
        return abilityState.fetchView(this)
    }

    override fun fetchDeathCause(): DeathCause {
        return deathCause
    }

    override fun fetchPersistentAbilities(): MutableList<Ability>{
        return persistentAbilities
    }

    override fun defineTeammates(teammates: List<String>) {
        this.teammates = listOf()
    }

    override fun definePossibleTargetPlayers(targetPlayers: List<String>) {
        possibleTargetPlayers = targetPlayers
    }

    override fun defineDefenseState(defenseState: DefenseState) {
        this.defenseState = defenseState
    }

    override fun defineTargetPlayer(targetPlayer: Player){
        if(targetPlayers.isEmpty()){
            targetPlayers.add(targetPlayer)
        }
        else{
            targetPlayers[0] = targetPlayer
        }
    }

    override fun definePersistentAbilities(abilities: MutableList<Ability>) {
        persistentAbilities = abilities
    }

    override fun addPersistentAbility(ability: Ability) {
        persistentAbilities.add(ability)
    }

    override fun removePersistentAbility(ability: Ability) {
        persistentAbilities.remove(ability)
    }

    override fun notifyAbilityUsed(targetPlayers: List<Player>){
        targetPlayers.forEach {
            this.targetPlayers.add(it)
            it.visitedBy(this)
        }
        abilityState.useAbility(this)
    }

    override fun notifyKilledPlayer(deathCause: DeathCause) {
        this.deathCause = deathCause
        signalEvent(PlayerEventEnum.KilledPlayer)
    }

    override fun receiveAbility(ability: Ability) {
        abilitiesUsedOnMe.add(ability)
    }

    override fun receiveAttack(villagerAttackAbility: VillagerAttackAbility) {
        defenseState.receiveDamage(this,villagerAttackAbility)
    }

    override fun receiveAttack(werewolfAttackAbility: WerewolfAttackAbility) {
        defenseState.receiveDamage(this,werewolfAttackAbility)
    }

    override fun resetDefenseState() {
        defenseState = NoDefense()
    }

    override fun resetVisitors() {
        visitors = mutableListOf()
    }

    override fun visitedBy(visitor: Player) {
        visitors.add(visitor)
    }

    override fun cancelAbility() {
        usedAbilities.forEach{
            it.cancel()
        }
    }

    override fun turnSetUp() {
        abilitiesUsedOnMe = mutableListOf()
        usedAbilities = mutableListOf()
        targetPlayers.clear()
        abilityState.turnSetUp(this)
    }

    fun signalEvent(event: PlayerEventEnum) {
        this.event = event
        onActionSubject.notify(PlayerSignal(this))
    }

    fun defineAbilityState(abilityState: AbilityState){
        this.abilityState = abilityState
    }

    open fun resolveFetchView(): FragmentEnum {
        return FragmentEnum.PlayerGridFragment
    }

    open fun resolveFetchTargetPlayers(): TargetPlayersEnum{
        return TargetPlayersEnum.NoTargetPlayers
    }

    open fun addUsedAbility(){}

    open fun applyDamage(deathCause: DeathCause) {
        notifyKilledPlayer(deathCause)
    }

}

abstract class WerewolfTeamPlayer(playerName: String): AbstractPlayer(playerName){

    override fun fetchFragment(): FragmentEnum {
        return FragmentEnum.WerewolfPlayerFragment
    }

    override fun receiveAttack(werewolfAttackAbility: WerewolfAttackAbility) {

    }

    override fun resolveFetchTargetPlayers(): TargetPlayersEnum {
        return TargetPlayersEnum.WerewolfTargets
    }

    override fun defineTeammates(teammates: List<String>){
        this.teammates = teammates
    }

}