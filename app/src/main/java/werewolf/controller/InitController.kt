package werewolf.controller

import com.example.observer.Observer
import werewolf.model.InitModel
import werewolf.view.InitActivity
import werewolf.view.InitUiEvent

interface InitController{
    fun setInitView(initActivity: InitActivity)
}

class InitControllerImpl(
    private val initModel: InitModel
): InitController {
    private lateinit var initActivity: InitActivity

    override fun setInitView(initActivity: InitActivity) {
        this.initActivity = initActivity
        initActivity.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<InitUiEvent> =
        Observer { value ->
            when (value) {
                InitUiEvent.AddPlayer -> addPlayer()
                InitUiEvent.RemovePlayer -> removePlayer()
                InitUiEvent.StartGame -> startGame()
            }
        }

    private fun addPlayer() {
        val name = initActivity.getEditTextName()
        initModel.addPlayer(name)
    }

    private fun removePlayer() {
        val index = obtainIndex()
        initModel.removePlayer(index)
    }

    private fun startGame() {
        if(initModel.players().isNotEmpty()){
            initActivity.startGame()
        }
    }

    private fun obtainIndex(): Int {
        return initModel.players().indexOfFirst { it == initActivity.getPlayerToRemove() }
    }
}