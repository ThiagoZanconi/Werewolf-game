package werewolf.view.howtoplay

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import werewolf.model.Roles
import werewolf.view.R
import werewolf.view.RoleNameProvider
import werewolf.view.fragments.ImageFragment

class RoleItemViewHolder(view: View):RecyclerView.ViewHolder(view){

    private val role = view.findViewById<TextView>(R.id.roleName)
    private val description = view.findViewById<TextView>(R.id.roleDescriptionTextView)
    private val imageDisplayButton = view.findViewById<ImageButton>(R.id.btnOpenImage)

    fun render(role: Roles, fragmentManager: FragmentManager){
        this.role.text = RoleNameProvider.getRoleName(role)
        description.text = RoleDescriptionProvider.getRoleDescription(role)
        imageDisplayButton.setOnClickListener{ initFragment(role, fragmentManager) }
    }

    private fun initFragment(role: Roles, fragmentManager: FragmentManager){
        val fragment = ImageFragment(role)
        fragmentManager.beginTransaction()
            .replace(R.id.OptionFragment, fragment)
            .commit()
    }
}