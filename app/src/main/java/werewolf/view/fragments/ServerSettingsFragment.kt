package werewolf.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

class ServerSettingsFragment: Fragment(){

    private val gameSettings: GameSettings = GameSettingsImpl
    private lateinit var recyclerView: RecyclerView
    private lateinit var doneButton: Button
    private var rewardedAd: RewardedAd? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        initComponents(view)
        initListeners()
        loadInterstitial()

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

    private fun loadInterstitial() {
        RewardedAd.load(
            requireContext(),
            "ca-app-pub-9153943970818884/3027351452",
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

    private fun showAdThenNavigate() {
        rewardedAd?.show(requireActivity()) {
            startGame()
        }
    }

}