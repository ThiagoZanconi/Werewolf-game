package werewolf.view.howtoplay

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import werewolf.model.Roles
import werewolf.view.R
import werewolf.view.RoleProvider
import werewolf.view.fragments.ImageFragment

class RoleItemViewHolder(view: View):RecyclerView.ViewHolder(view){

    private val role = view.findViewById<TextView>(R.id.roleName)
    private val imageDisplayButton = view.findViewById<ImageButton>(R.id.btnOpenImage)
    private val premiumTextViewContainer = view.findViewById<FrameLayout>(R.id.premiumTextViewContainer)

    init {
        premiumTextViewContainer.visibility = View.INVISIBLE
    }

    fun render(role: Roles, fragmentManager: FragmentManager){
        this.role.text = RoleProvider.getRoleName(role)
        imageDisplayButton.setOnClickListener{ initFragment(role, fragmentManager) }
        if (role in RoleProvider.getPremiumRoles()) {
            premiumTextViewContainer.visibility = View.VISIBLE
        } else {
            premiumTextViewContainer.visibility = View.INVISIBLE
        }
    }

    private fun initFragment(role: Roles, fragmentManager: FragmentManager){
        val fragment = ImageFragment(role)
        fragmentManager.beginTransaction()
            .replace(R.id.OptionFragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}