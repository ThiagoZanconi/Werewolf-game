package werewolf.view.settings

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.observer.Subject
import werewolf.model.Roles
import werewolf.view.R
import werewolf.view.RoleNameProvider

class SettingsItemViewHolder(private val view: View,private val onActionSubject: Subject<Pair<Roles,Int>>):RecyclerView.ViewHolder(view){

    private val role = view.findViewById<TextView>(R.id.roleName)
    private val spinner = view.findViewById<Spinner>(R.id.spinnerOptions)

    fun render(role: Roles, playersSize: Int){
        println("Role rendered: $role")
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
        initSpinnerListener(role)
    }

    private fun initSpinnerListener(role: Roles) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                onActionSubject.notify(Pair(role, spinner.selectedItem.toString().toInt()))
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}