package werewolf.model

import werewolf.view.InitActivity

interface InitModel{
    fun setInitView(initActivity: InitActivity)
    fun players(): MutableList<String>
    fun addPlayer(name: String)
    fun removePlayer(index: Int)
}

class InitModelImpl: InitModel{
    private lateinit var initActivity: InitActivity
    private val players: MutableList<String> = mutableListOf()

    override fun setInitView(initActivity: InitActivity) {
        this.initActivity = initActivity
    }

    override fun players(): MutableList<String> {
        return players
    }

    override fun addPlayer(name: String) {
        if(name !in players && name!=""){
            players.add(name)
            initActivity.addPlayer(name)
        }
    }

    override fun removePlayer(index: Int) {
        val name = players.removeAt(index)
        initActivity.removePlayer(index,name)
    }
}