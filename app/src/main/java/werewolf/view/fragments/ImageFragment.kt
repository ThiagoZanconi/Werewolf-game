package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import werewolf.model.Roles
import werewolf.view.R
import werewolf.view.RoleNameProvider
import werewolf.view.howtoplay.RoleImageProvider

class ImageFragment(private val role: Roles): Fragment(){

    private lateinit var imageView: ImageView
    private lateinit var titleLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_role_image_display, container, false)
        initComponents(view)
        return view
    }

    private fun initComponents(view: View) {
        imageView = view.findViewById(R.id.roleImageLabel)
        imageView.setImageResource(RoleImageProvider.getRoleImage(role))
        titleLabel = view.findViewById(R.id.titleLabel)
        titleLabel.text = RoleNameProvider.getRoleName(role)
    }

}