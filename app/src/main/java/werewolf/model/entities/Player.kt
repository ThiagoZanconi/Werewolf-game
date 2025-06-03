package werewolf.model.entities

import androidx.fragment.app.Fragment
import com.example.observer.Observable
import com.example.observer.Subject
import werewolf.model.Roles
import werewolf.view.GameUiEvent
import werewolf.view.TargetPlayersEnum
import werewolf.view.TargetPlayersSignal
import werewolf.view.fragments.PlayerGridFragment
import werewolf.view.fragments.WerewolfTeamFragment

enum class DeathCause{
    HANGED, MAULED, SHOT, BURNT, EXPLODED
}

interface Player{
    val playerObservable: Observable<PlayerSignal>

    fun fetchPlayerName(): String
    fun fetchRole(): Roles
    fun fetchTeammates(): TargetPlayersEnum
    fun fetchTargetPlayersOptions(): TargetPlayersEnum
    fun fetchTargetPlayers(): List<Player>
    fun fetchEvent(): PlayerEventEnum
    fun fetchAbilityState(): AbilityStateEnum
    fun fetchAbilitiesUsedOnMe(): List<Ability>
    fun fetchUsedAbilities(): List<Ability>
    fun fetchVisitors(): List<Player>

    //Returns ability used for logs
    fun fetchUsedAbility(index: Int): String?
    fun fetchView(onActionSubject: Subject<GameUiEvent>, targetPlayersOnActionSubject: Subject<TargetPlayersSignal>): Fragment
    fun fetchDeathCause(): DeathCause
    fun fetchImageSrc(): Int
    fun defineDefenseState(defenseState: DefenseState)
    fun defineTargetPlayer(targetPlayer: Player)

    //Ability delegated to abilityState contrary case
    fun notifyAbilityUsed(targetPlayer: Player?)
    fun receiveAbility(ability: Ability)
    fun visitedBy(visitor: Player)
    fun resetVisitors()
    fun turnSetUp()
    fun receiveDamage(deathCause: DeathCause)
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
    protected var targetPlayers: MutableList<Player> = mutableListOf()

    protected val onActionSubject = Subject<PlayerSignal>()
    override val playerObservable: Observable<PlayerSignal> = onActionSubject

    override fun fetchPlayerName(): String{
        return playerName
    }

    override fun fetchRole(): Roles {
        return role
    }

    override fun fetchTeammates(): TargetPlayersEnum {
        return TargetPlayersEnum.SetNoTargetPlayers
    }

    override fun fetchTargetPlayersOptions(): TargetPlayersEnum {
        return abilityState.fetchTargetPlayers(this)
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

    override fun fetchUsedAbility(index: Int): String? {
        return if (usedAbilities.isNotEmpty())
            usedAbilities[index].fetchAbilityName()
        else
            null
    }

    override fun fetchVisitors(): List<Player>{
        return visitors
    }

    override fun fetchView(onActionSubject: Subject<GameUiEvent>, targetPlayersOnActionSubject: Subject<TargetPlayersSignal>): Fragment {
        return PlayerGridFragment(onActionSubject,this,targetPlayersOnActionSubject)
    }

    override fun fetchDeathCause(): DeathCause {
        return deathCause
    }

    override fun defineDefenseState(defenseState: DefenseState) {
        this.defenseState = defenseState
    }

    override fun defineTargetPlayer(targetPlayer: Player){
        targetPlayers[0] = targetPlayer
    }

    override fun notifyAbilityUsed(targetPlayer: Player?){
        if(targetPlayer!=null){
            targetPlayers[0] = targetPlayer
        }
        targetPlayer?.visitedBy(this)
        abilityState.useAbility(this)
    }

    override fun receiveAbility(ability: Ability) {
        abilitiesUsedOnMe.add(ability)
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

    open fun resolveFetchTargetPlayers(): TargetPlayersEnum{
        return TargetPlayersEnum.SetNoTargetPlayers
    }

    open fun addUsedAbility(){}

    open fun applyDamage(deathCause: DeathCause) {
        notifyKilledPlayer(deathCause)
    }

}

abstract class WerewolfTeamPlayer: AbstractPlayer(){

    override fun fetchView(onActionSubject: Subject<GameUiEvent>, targetPlayersOnActionSubject: Subject<TargetPlayersSignal>): Fragment {
        return WerewolfTeamFragment(onActionSubject,this,targetPlayersOnActionSubject)
    }

}