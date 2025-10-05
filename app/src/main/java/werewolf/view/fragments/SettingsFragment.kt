package werewolf.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import werewolf.view.GameActivityImpl
import werewolf.view.R
import werewolf.model.GameSettings
import werewolf.model.GameSettingsImpl
import werewolf.view.settings.SettingsAdapter

class SettingsFragment: Fragment(){

    private val gameSettings: GameSettings = GameSettingsImpl
    private lateinit var recyclerView: RecyclerView
    private lateinit var doneButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        initComponents(view)
        initListeners()

        return view
    }

    private fun initComponents(view: View) {
        initRecyclerView(view,gameSettings)
        doneButton = view.findViewById(R.id.doneButton)
    }

    private fun initListeners(){
        doneButton.setOnClickListener { startGame() }
    }

    private fun initRecyclerView(view: View, gameSettings: GameSettings){
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(view.context,2)
        val adapter = SettingsAdapter(gameSettings)
        recyclerView.adapter = adapter
    }

    private fun startGame(){
        val intent = Intent(requireContext(), GameActivityImpl::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}