package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
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

    }

    private fun initFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.OptionFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

}


