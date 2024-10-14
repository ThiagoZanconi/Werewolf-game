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
    val playerObservable: Observable<PlayerSignal>

    fun fetchPlayerName(): String
    fun fetchRole(): String
    fun fetchTargetPlayers(): List<Player>
    fun fetchTargetPlayer(): Player?
    fun fetchEvent(): PlayerEventEnum
    fun fetchAbilityState(): String
    /*
    * Returns ability used in game
    * Null if player didn't use ability
    * Ability delegated to abilityState contrary case
     */
    fun useAbility(): Ability?
    /*
    * Returns ability used for logs
     */
    fun fetchUsedAbility(): String?
    fun fetchView(onActionSubject: Subject<GameUiEvent>): Fragment
    fun fetchDeathCause(): DeathCause
    fun fetchImageSrc(): Int
    fun defineDefenseState(defenseState: DefenseState)
    fun defineTargetPlayers(targetPlayers: List<Player>)
    fun defineTargetPlayer(targetPlayer: Player?)
    fun turnSetUp()
    fun receiveDamage(deathCause: DeathCause)
    fun receiveAbility(ability: Ability)
    fun notifyKilledPlayer(deathCause: DeathCause)
    fun resetDefenseState()
    fun cancelAbility()
}

abstract class AbstractPlayer: Player{
    protected abstract val playerName: String
    protected abstract val role: String
    protected open var defenseState: DefenseState = NoDefense()
    protected open var abilityState: AbilityState = Neutral()
    protected lateinit var event: PlayerEventEnum
    protected lateinit var deathCause: DeathCause
    protected open var usedAbility: Ability? = null

    protected var abilitiesUsedOnMe: MutableList<Ability> = mutableListOf()
    protected var targetPlayer: Player? = null
    private lateinit var targetPlayers: List<Player>

    protected val onActionSubject = Subject<PlayerSignal>()
    override val playerObservable: Observable<PlayerSignal> = onActionSubject

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

    override fun useAbility(): Ability?{
        return if(targetPlayer!=null){
            abilityState.useAbility(this)
        } else
            null
    }

    override fun fetchUsedAbility(): String? {
        return usedAbility?.fetchAbilityName()
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

    override fun defineTargetPlayers(targetPlayers: List<Player>) {
        this.targetPlayers = targetPlayers
    }

    override fun defineTargetPlayer(targetPlayer: Player?) {
        this.targetPlayer = targetPlayer
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
        usedAbility?.cancel()
    }

    override fun turnSetUp() {
        signalEvent(PlayerEventEnum.SetNoTargets)
    }

    protected fun signalEvent(event: PlayerEventEnum) {
        this.event = event
        onActionSubject.notify(PlayerSignal(this))
    }

    abstract fun resolveAbility(): Ability?

    fun defineAbilityState(abilityState: AbilityState){
        this.abilityState = abilityState
    }

    open fun applyDamage(deathCause: DeathCause) {
        notifyKilledPlayer(deathCause)
    }

}