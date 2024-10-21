package werewolf.view.howtoplay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import werewolf.model.Roles
import werewolf.view.R

class RolesDescriptionAdapter(private val rolesList: Array<Roles>): RecyclerView.Adapter<RoleItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RoleItemViewHolder(layoutInflater.inflate(R.layout.item_role_description,parent,false))
    }

    override fun getItemCount(): Int {
        return rolesList.size
    }

    override fun onBindViewHolder(holder: RoleItemViewHolder, position: Int) {
        holder.render(rolesList[position])
    }

}