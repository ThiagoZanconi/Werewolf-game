package werewolf.view.settings

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import werewolf.model.GameSettings
import werewolf.model.Roles
import werewolf.view.R
import werewolf.view.RoleProvider

class SettingsItemViewHolder(private val view: View, private val gameSettings: GameSettings):RecyclerView.ViewHolder(view){

    private val role = view.findViewById<TextView>(R.id.roleName)
    private val spinner = view.findViewById<Spinner>(R.id.spinnerOptions)

    fun render(role: Roles, playersSize: Int){

        this.role.text = RoleProvider.getRoleName(role)
        spinner.onItemSelectedListener = null
        spinner.adapter = null

        if(role in RoleProvider.getPremiumRoles()){
            initPremiumRoleSpinner()
        }
        else{
            initSpinner(role,playersSize)
        }

    }

    private fun initSpinner(role: Roles,playersSize: Int){
        val options: MutableList<Int> = mutableListOf()
        for(i in 0 .. playersSize){
            options.add(i)
        }
        val adapter = ArrayAdapter(view.context, R.layout.item_spinner, options)
        spinner.adapter = adapter
        spinner.setSelection(gameSettings.fetchRoleQuantity(role))
        initSpinnerListener(role)
    }

    private fun initPremiumRoleSpinner(){
        val adapter = object : ArrayAdapter<String>(
            view.context,
            R.layout.item_spinner,
            listOf("P")
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val tv = super.getView(position, convertView, parent) as TextView
                tv.setTextColor(Color.parseColor("#FFD700"))
                return tv
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val tv = super.getDropDownView(position, convertView, parent) as TextView
                tv.setTextColor(Color.parseColor("#FFD700"))
                return tv
            }
        }
        spinner.adapter = adapter
        spinner.setSelection(0)
    }

    private fun initSpinnerListener(role: Roles) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selected = parent.getItemAtPosition(position) as Int
                val current = gameSettings.fetchRoleQuantity(role)

                if (selected != current) {
                    gameSettings.setRoleQuantity(role, selected)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}