package werewolf.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import werewolf.model.Roles
import werewolf.view.GameActivityImpl
import werewolf.view.R
import werewolf.view.settings.SettingAdapter

class SettingsFragment(
    private val playerSize: Int
): Fragment(){
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
        initRecyclerView(view)
        doneButton = view.findViewById(R.id.doneButton)
    }

    private fun initListeners(){
        doneButton.setOnClickListener { startGame() }
    }

    private fun initRecyclerView(view: View){
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = SettingAdapter(Roles.values(),playerSize)
    }

    private fun startGame(){
        val intent = Intent(requireContext(), GameActivityImpl::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}