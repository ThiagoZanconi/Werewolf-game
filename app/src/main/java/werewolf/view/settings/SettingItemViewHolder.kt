package werewolf.view.settings

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import werewolf.model.Roles
import werewolf.view.R
import java.io.File

class SettingItemViewHolder(private val view: View):RecyclerView.ViewHolder(view){

    private val role = view.findViewById<TextView>(R.id.roleName)
    private val spinner = view.findViewById<Spinner>(R.id.spinnerOptions)

    fun render(role: Roles, playersSize: Int){
        initSpinner(playersSize)
        this.role.text = role.toString()
    }

    private fun initSpinner(playersSize: Int){
        val options: MutableList<Int> = mutableListOf()
        for(i in 0 .. playersSize){
            options.add(i)
        }
        val adapter = ArrayAdapter(view.context, R.layout.item_spinner, options)
        spinner.adapter = adapter
        spinner.setSelection(playersSize)
        initSpinnerListener()
    }

    private fun initSpinnerListener() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                addRoleRestriction()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun addRoleRestriction(){
        val settings = File(view.context.cacheDir, "werewolfSettings.txt")
        val lines = settings.readLines().toMutableList()
        lines[getRoleIndex()] = spinner.selectedItem.toString()
    }

    private fun getRoleIndex(): Int{
        var encontre = false
        var i = 0
        while(!encontre){
            if(role.text == Roles.values()[i].toString()){
                encontre = true
            }
            else{
                i++
            }
        }
        return i
    }

}