package werewolf.view.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import werewolf.model.Roles
import werewolf.view.R

class SettingAdapter(private val rolesList: Array<Roles>, private val playerSize: Int): RecyclerView.Adapter<SettingItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SettingItemViewHolder(layoutInflater.inflate(R.layout.item_setting_config,parent,false))
    }

    override fun getItemCount(): Int {
        return rolesList.size
    }

    override fun onBindViewHolder(holder: SettingItemViewHolder, position: Int) {
        holder.render(rolesList[position],playerSize)
    }

}