package werewolf.view.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import werewolf.model.GameSettingsImpl
import werewolf.view.R
import werewolf.view.ViewInjector

class ActivityMainFragment : Fragment(){

    private lateinit var hostServerButton: Button
    private lateinit var howToPlayButton: Button
    private lateinit var upgradeButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewLifecycleOwner.lifecycleScope.launch {
            GameSettingsImpl.loadMaxRoleQuantitySettings()
        }
        val view = inflater.inflate(R.layout.fragment_activity_main, container, false)
        initComponents(view)
        initListeners()
        return view
    }

    private fun initComponents(view: View) {
        hostServerButton = view.findViewById(R.id.hostServer)
        howToPlayButton = view.findViewById(R.id.howToPlayButton)
        upgradeButton = view.findViewById(R.id.updateButton)
    }

    private fun initListeners() {
        hostServerButton.setOnClickListener { hostServer() }
        howToPlayButton.setOnClickListener { goToHowPlay() }
        upgradeButton.setOnClickListener{ upgrade() }
    }

    private fun hostServer(){
        val serverFragment = ServerFragment()
        initServer(serverFragment)
        initFragment(serverFragment)
    }

    private fun initServer(fragment: ServerFragment) {
        ViewInjector.initServer(fragment)
    }

    private fun goToHowPlay(){
        initFragment(HowToPlayFragment())
    }

    private fun upgrade(){
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_description, null)
        dialog.setContentView(view)
        val textView: TextView = view.findViewById(R.id.descriptionTextView)
        textView.text = "Coming Soon!"
        dialog.show()
    }

    private fun initFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.OptionFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

}