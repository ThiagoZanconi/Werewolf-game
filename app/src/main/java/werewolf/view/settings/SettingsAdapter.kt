package werewolf.view.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import werewolf.model.Roles
import werewolf.view.R

class SettingsAdapter(
    private val rolesList: Array<Roles>,
    private val roleQuantitySettings: RoleQuantitySettings
): RecyclerView.Adapter<SettingsItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SettingsItemViewHolder(layoutInflater.inflate(R.layout.item_setting_config,parent,false),roleQuantitySettings)
    }

    override fun getItemCount(): Int {
        return rolesList.size
    }

    override fun onBindViewHolder(holder: SettingsItemViewHolder, position: Int) {
        holder.render(rolesList[position],roleQuantitySettings.getRoleMaxQuantity(rolesList[position]))
    }
}