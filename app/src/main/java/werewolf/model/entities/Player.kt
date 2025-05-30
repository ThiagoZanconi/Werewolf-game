package werewolf.model.entities

import androidx.fragment.app.Fragment
import com.example.observer.Observable
import com.example.observer.Subject
import werewolf.model.Roles
import werewolf.view.GameUiEvent
import werewolf.view.fragments.PlayerGridFragment
import werewolf.view.fragments.WerewolfTeamFragment

enum class DeathCause{
    HANGED, MAULED, SHOT, BURNT
}

interface Player{
    val playerObservable: Observable<PlayerSignal>

    fun fetchPlayerName(): String
    fun fetchRole(): Roles
    fun fetchTeammates(): List<Player>
    fun fetchTargetPlayers(): List<Player>
    fun fetchTargetPlayer(): Player?
    fun fetchEvent(): PlayerEventEnum
    fun fetchAbilityState(): String
    fun fetchAbilitiesUsedOnMe(): List<Ability>
    fun fetchUsedAbilities(): List<Ability>

    //Returns ability used for logs
    fun fetchUsedAbility(index: Int): String?
    fun fetchView(onActionSubject: Subject<GameUiEvent>): Fragment
    fun fetchDeathCause(): DeathCause
    fun fetchImageSrc(): Int
    fun defineDefenseState(defenseState: DefenseState)
    fun defineTeammates(team: List<Player>)
    fun defineTargetPlayers(targetPlayers: List<Player>)
    fun defineTargetPlayer(targetPlayer: Player?)

    //Ability delegated to abilityState contrary case
    fun notifyAbilityUsed(targetPlayer: Player?)
    fun visitedBy(visitor: Player)
    fun resetVisitors()
    fun turnSetUp()
    fun receiveDamage(deathCause: DeathCause)
    fun receiveAbility(ability: Ability)
    fun notifyKilledPlayer(deathCause: DeathCause)
    fun resetDefenseState()
    fun cancelAbility()
}

abstract class AbstractPlayer: Player{
    protected abstract val playerName: String
    protected abstract val role: Roles
    protected open var defenseState: DefenseState = NoDefense()
    protected open var abilityState: AbilityState = Neutral()
    protected lateinit var event: PlayerEventEnum
    protected lateinit var deathCause: DeathCause
    protected open var usedAbilities: MutableList<Ability> = mutableListOf()
    protected var visitors: MutableList<Player> = mutableListOf()
    private var abilitiesUsedOnMe: MutableList<Ability> = mutableListOf()
    protected var targetPlayer: Player? = null
    private var targetPlayers: List<Player> = listOf()
    private lateinit var teammates: List<Player>

    protected val onActionSubject = Subject<PlayerSignal>()
    override val playerObservable: Observable<PlayerSignal> = onActionSubject

    override fun fetchPlayerName(): String{
        return playerName
    }

    override fun fetchRole(): Roles {
        return role
    }

    override fun fetchTeammates(): List<Player> {
        return teammates
    }

    override fun fetchTargetPlayers(): List<Player> {
        return targetPlayers
    }

    override fun fetchTargetPlayer(): Player? {
        return targetPlayer
    }

    override fun fetchEvent(): PlayerEventEnum {
        return event
    }

    override fun fetchAbilityState(): String{
        return abilityState.getAbilityState()
    }
    override fun fetchAbilitiesUsedOnMe(): List<Ability>{
        return abilitiesUsedOnMe
    }

    override fun fetchUsedAbilities(): List<Ability>{
        return usedAbilities
    }

    override fun notifyAbilityUsed(targetPlayer: Player?){
        this.targetPlayer = targetPlayer
        abilityState.useAbility(this)
    }

    override fun fetchUsedAbility(index: Int): String? {
        return if (usedAbilities.isNotEmpty())
            usedAbilities[index].fetchAbilityName()
        else
            null
    }

    override fun fetchView(onActionSubject: Subject<GameUiEvent>): Fragment {
        return PlayerGridFragment(onActionSubject,this)
    }

    override fun fetchDeathCause(): DeathCause {
        return deathCause
    }

    override fun defineDefenseState(defenseState: DefenseState) {
        this.defenseState = defenseState
    }

    override fun defineTeammates(team: List<Player>) {
        teammates = team
    }

    override fun defineTargetPlayers(targetPlayers: List<Player>) {
        this.targetPlayers = targetPlayers
    }

    override fun defineTargetPlayer(targetPlayer: Player?) {
        targetPlayer?.visitedBy(this)
        this.targetPlayer = targetPlayer
    }

    override fun visitedBy(visitor: Player) {
        visitors.add(visitor)
    }

    override fun resetVisitors() {
        visitors = mutableListOf()
    }

    override fun receiveDamage(deathCause: DeathCause) {
        defenseState.receiveDamage(this,deathCause)
    }

    override fun receiveAbility(ability: Ability) {
        abilitiesUsedOnMe.add(ability)
    }

    override fun notifyKilledPlayer(deathCause: DeathCause) {
        this.deathCause = deathCause
        signalEvent(PlayerEventEnum.KilledPlayer)
    }

    override fun resetDefenseState() {
        defenseState = NoDefense()
    }

    override fun cancelAbility() {
        usedAbilities.forEach{
            it.cancel()
        }
    }

    override fun turnSetUp() {
        abilitiesUsedOnMe = mutableListOf()
    }

    protected fun signalEvent(event: PlayerEventEnum) {
        this.event = event
        onActionSubject.notify(PlayerSignal(this))
    }

    fun defineAbilityState(abilityState: AbilityState){
        this.abilityState = abilityState
    }

    open fun addUsedAbility(){}

    open fun applyDamage(deathCause: DeathCause) {
        notifyKilledPlayer(deathCause)
    }
}

abstract class WerewolfTeamPlayer: AbstractPlayer(){
    override fun turnSetUp() {
        super.turnSetUp()
        signalEvent(PlayerEventEnum.SetWerewolfTeammates)
    }

    override fun fetchView(onActionSubject: Subject<GameUiEvent>): Fragment {
        return WerewolfTeamFragment(onActionSubject,this)
    }
}