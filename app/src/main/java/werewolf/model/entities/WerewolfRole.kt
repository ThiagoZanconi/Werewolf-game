package werewolf.model.entities

import androidx.fragment.app.Fragment
import com.example.observer.Observable
import com.example.observer.Subject
import werewolf.view.GameUiEvent
import werewolf.view.fragments.PlayerGridFragment

enum class DeathCause{
    HANGED, MAULED, SHOT
}

interface Player{
    val playerObservable: Observable<EventSignal>

    fun fetchPlayerName(): String
    fun fetchRole(): String
    fun fetchTargetPlayers(): List<Player>
    fun fetchTargetPlayer(): Player?
    fun fetchEvent(): PlayerEventEnum
    fun fetchAbilityState(): String
    /*
    * Returns ability used in game
     */
    fun fetchEndOfRoundAbility(): EndOfRoundAbility?
    /*
    * Returns ability used for logs
     */
    fun fetchUsedAbility(): String?
    fun fetchView(onActionSubject: Subject<GameUiEvent>): Fragment
    fun fetchDeathCause(): DeathCause
    fun fetchImageSrc(): Int

    fun defineDefenseState(defenseState: DefenseState)
    fun defineAbilityState(abilityState: AbilityState)
    fun defineTargetPlayers(targetPlayers: List<Player>)
    fun defineTargetPlayer(targetPlayer: Player?)

    fun turnSetUp()
    fun receiveDamage(deathCause: DeathCause)
    fun receiveAbility(ability: EndOfRoundAbility)
    fun notifyKilledPlayer(deathCause: DeathCause)
    fun resetAbilityState()
    fun resetDefenseState()
    fun nullifyAbility()
    fun signalEvent(event: PlayerEventEnum)
}

abstract class AbstractPlayer: Player{
    protected abstract val playerName: String
    protected abstract val role: String
    protected open var defenseState: DefenseState = NoDefense()
    protected open var abilityState: AbilityState = Neutral()
    protected lateinit var event: PlayerEventEnum
    protected lateinit var deathCause: DeathCause
    protected open var ability: EndOfRoundAbility? = null

    protected var abilitiesUsedOnMe: MutableList<EndOfRoundAbility> = mutableListOf()
    protected var targetPlayer: Player? = null
    private lateinit var targetPlayers: List<Player>

    protected val onActionSubject = Subject<EventSignal>()
    override val playerObservable: Observable<EventSignal> = onActionSubject

    override fun fetchPlayerName(): String{
        return playerName
    }

    override fun fetchRole(): String{
        return role
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

    override fun fetchEndOfRoundAbility(): EndOfRoundAbility?{
        return abilityState.useAbility(this)
    }

    override fun fetchUsedAbility(): String? {
        return ability?.fetchAbilityName()
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

    override fun defineAbilityState(abilityState: AbilityState){
        this.abilityState = abilityState
    }

    override fun defineTargetPlayers(targetPlayers: List<Player>) {
        this.targetPlayers = targetPlayers
    }

    override fun defineTargetPlayer(targetPlayer: Player?) {
        this.targetPlayer = targetPlayer
    }

    override fun receiveDamage(deathCause: DeathCause) {
        defenseState.receiveDamage(this,deathCause)
    }

    override fun receiveAbility(ability: EndOfRoundAbility) {
        abilitiesUsedOnMe.add(ability)
    }

    override fun notifyKilledPlayer(deathCause: DeathCause) {
        this.deathCause = deathCause
        signalEvent(PlayerEventEnum.KilledPlayer)
    }

    override fun resetAbilityState(){
        abilityState.setAbilityState(this, Neutral())
    }

    override fun resetDefenseState() {
        defenseState = NoDefense()
    }

    override fun nullifyAbility() {
        ability?.nullify()
    }

    override fun signalEvent(event: PlayerEventEnum) {
        this.event = event
        onActionSubject.notify(EventSignal(this))
    }

    override fun turnSetUp() {
        signalEvent(PlayerEventEnum.SetNoTargets)
    }

    abstract fun resolveAbility(): EndOfRoundAbility?

    open fun cooldownTimer(){

    }

    fun applyDamage(deathCause: DeathCause) {
        notifyKilledPlayer(deathCause)
    }
}