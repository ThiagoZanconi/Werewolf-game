package werewolf.view.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import werewolf.view.R
import werewolf.model.GameSettings
import werewolf.model.GameSettingsImpl
import werewolf.view.GameActivityImpl
import werewolf.view.settings.SettingsAdapter

class ServerSettingsFragment(private var rewardedAd: RewardedAd?): Fragment(){

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
        doneButton.setOnClickListener { showAdThenNavigate() }
    }

    private fun initRecyclerView(view: View, gameSettings: GameSettings){
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(view.context,2)
        val adapter = SettingsAdapter(gameSettings)
        recyclerView.adapter = adapter
    }

    private fun startGame(){
        val intent = Intent(requireActivity().applicationContext, GameActivityImpl::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showAdThenNavigate() {
        if(rewardedAd!=null){
            doneButton.text = "Loading..."
            doneButton.isEnabled = false
            rewardedAd!!.show(requireActivity()) {
                startGame()
            }
        }
        else{
            loadInterstitial()
            val dialog = Dialog(requireContext())
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_description, null)
            dialog.setContentView(view)
            val textView: TextView = view.findViewById(R.id.roleDescriptionTextView)
            textView.text = "Loading ad...! Review your internet connection"
            dialog.show()
        }

    }

    private fun loadInterstitial() {
        //"ca-app-pub-3940256099942544/5224354917" Test ID
        //"ca-app-pub-9153943970818884/3027351452" Original ID
        RewardedAd.load(
            requireContext(),
            "ca-app-pub-3940256099942544/5224354917",
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }

}