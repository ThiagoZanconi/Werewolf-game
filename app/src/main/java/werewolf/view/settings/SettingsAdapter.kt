package werewolf.view.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import werewolf.model.GameSettings
import werewolf.view.R

class SettingsAdapter(
    private val gameSettings: GameSettings
): RecyclerView.Adapter<SettingsItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SettingsItemViewHolder(layoutInflater.inflate(R.layout.item_setting_config,parent,false),gameSettings)
    }

    override fun getItemCount(): Int {
        return gameSettings.size()
    }

    override fun onBindViewHolder(holder: SettingsItemViewHolder, position: Int) {
        holder.render(gameSettings.indexOfRole(position),gameSettings.fetchRoleMaxQuantity(gameSettings.indexOfRole(position)))
    }
}