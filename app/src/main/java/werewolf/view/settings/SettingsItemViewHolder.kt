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

class SettingsItemViewHolder(private val view: View):RecyclerView.ViewHolder(view){

    private val role = view.findViewById<TextView>(R.id.roleName)
    private val spinner = view.findViewById<Spinner>(R.id.spinnerOptions)

    fun render(role: Roles, playersSize: Int){
        this.role.text = RoleNameProvider.getRoleName(role)
        initSpinner(role,playersSize)
    }

    private fun initSpinner(role: Roles,playersSize: Int){
        val options: MutableList<Int> = mutableListOf()
        for(i in 0 .. playersSize){
            options.add(i)
        }
        val adapter = ArrayAdapter(view.context, R.layout.item_spinner, options)
        spinner.adapter = adapter
        spinner.setSelection(playersSize)
        initMaxRoleRestriction(role,playersSize)
        initSpinnerListener(role)
    }

    private fun initMaxRoleRestriction(role: Roles,playersSize: Int){
        val settings = File(view.context.cacheDir, "werewolfSettings.txt")
        val lines = settings.readLines().toMutableList()
        lines[role.ordinal+1] = playersSize.toString()
        settings.writeText(lines.joinToString("\n"))
    }

    private fun initSpinnerListener(role: Roles) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                addRoleRestriction(role)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    /*
    *Writes the max amount of that role appearances that can be in that game
    *Line[0] is reserved for player names
     */
    private fun addRoleRestriction(role:Roles){
        val settings = File(view.context.cacheDir, "werewolfSettings.txt")
        val lines = settings.readLines().toMutableList()
        lines[role.ordinal+1] = spinner.selectedItem.toString()
        settings.writeText(lines.joinToString("\n"))
    }
}