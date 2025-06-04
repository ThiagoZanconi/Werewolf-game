package werewolf.controller

import werewolf.model.entities.DeathCause
import werewolf.view.MyApp
import werewolf.view.R

object DeathCauseProvider {
    fun getDeathCause(deathCause: DeathCause): String{
        return when(deathCause){
            DeathCause.HANGED -> MyApp.getAppContext().getString(R.string.was_hanged)
            DeathCause.MAULED -> MyApp.getAppContext().getString(R.string.was_mauled)
            DeathCause.SHOT -> MyApp.getAppContext().getString(R.string.was_shot)
            DeathCause.EXPLODED -> MyApp.getAppContext().getString(R.string.exploded)
        }
    }
}