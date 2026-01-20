package werewolf.view.fragments

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
        val appPackageName = "com.socialgamesclub.werewolf.premium"

        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            requireContext().startActivity(intent)

        } catch (e: ActivityNotFoundException) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            requireContext().startActivity(intent)
        }
    }

    private fun initFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.OptionFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

}