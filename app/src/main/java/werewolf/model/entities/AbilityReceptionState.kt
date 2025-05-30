package werewolf.model.entities

interface AbilityReceptionState{
    fun receiveAbility(ability: Ability)

}

class NormalAbilityReceptionState : AbilityReceptionState{
    override fun receiveAbility(ability: Ability){
        ability.resolve()
    }
}

class Elusive : AbilityReceptionState{
    override fun receiveAbility(ability: Ability){

    }
}