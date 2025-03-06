package werewolf.view.howtoplay

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import werewolf.model.Roles
import werewolf.view.R
import werewolf.view.RoleNameProvider

class RoleItemViewHolder(view: View):RecyclerView.ViewHolder(view){

    private val role = view.findViewById<TextView>(R.id.roleName)
    private val description = view.findViewById<TextView>(R.id.roleDescription)

    fun render(role: Roles){
        this.role.text = RoleNameProvider.getRoleName(role)
        description.text = RoleDescriptionProvider.getRoleDescription(role)
    }

}