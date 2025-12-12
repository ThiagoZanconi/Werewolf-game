package werewolf.view

import android.app.Application
import android.content.Context
import com.google.android.gms.ads.MobileAds

class MyApp : Application() {

    companion object {
        private lateinit var instance: MyApp

        fun getAppContext(): Context {
            return instance.applicationContext
        }
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        MobileAds.initialize(this)
    }
}