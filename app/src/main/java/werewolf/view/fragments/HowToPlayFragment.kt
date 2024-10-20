package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import werewolf.view.R

class HowToPlayFragment(
): Fragment(){
    private lateinit var rolesDescriptionButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_howtoplay, container, false)
        initComponents(view)
        initListeners()

        return view
    }

    private fun initComponents(view: View) {
        rolesDescriptionButton = view.findViewById(R.id.roleDescriptionButton)
    }

    private fun initListeners(){
        rolesDescriptionButton.setOnClickListener { goToRolesDescription() }
    }
    private fun goToRolesDescription(){

    }
}