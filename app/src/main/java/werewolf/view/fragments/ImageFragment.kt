package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import werewolf.model.Roles
import werewolf.view.R
import werewolf.view.RoleNameProvider
import werewolf.view.howtoplay.RoleImageProvider

class ImageFragment(private val role: Roles): Fragment(){

    private lateinit var imageView: ImageView
    private lateinit var titleLabel: TextView
    private lateinit var roleDescriptionButton: ImageButton
    private lateinit var roleDescriptionButtonContainer: FrameLayout
    private lateinit var textViewDescriptionLayout: LinearLayout

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
        roleDescriptionButton = view.findViewById(R.id.roleDescriptionButton)
        roleDescriptionButton.visibility = View.GONE
        textViewDescriptionLayout = view.findViewById(R.id.textViewDescriptionLayout)
        textViewDescriptionLayout.visibility = View.GONE
        roleDescriptionButtonContainer = view.findViewById(R.id.roleDescriptionButtonContainer)
        roleDescriptionButtonContainer.visibility = View.GONE
    }

}