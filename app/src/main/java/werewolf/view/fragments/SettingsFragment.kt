package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import werewolf.view.R
import werewolf.view.settings.RoleProvider
import werewolf.view.settings.SettingItemAdapter

class SettingsFragment(
    private val playerSize: Int
): Fragment(){
    private val unsavedChanges: Boolean = false
    private lateinit var recyclerView: RecyclerView

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
    }

    private fun initListeners(){}

    private fun initRecyclerView(view: View){
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = SettingItemAdapter(RoleProvider.roleList(),playerSize)
    }

    private fun saveSettings(){

    }

}