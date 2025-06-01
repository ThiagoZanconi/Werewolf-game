package werewolf.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import werewolf.model.Roles
import werewolf.view.R
import werewolf.view.howtoplay.RolesDescriptionAdapter

class RolesDescriptionFragment: Fragment(){
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rolesdescription, container, false)
        initComponents(view)

        return view
    }
    private fun initComponents(view: View) {
        initRecyclerView(view)
    }
    private fun initRecyclerView(view: View){
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = RolesDescriptionAdapter(Roles.entries.toTypedArray())
    }

}