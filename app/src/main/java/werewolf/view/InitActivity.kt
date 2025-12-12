package werewolf.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import werewolf.view.fragments.ActivityMainFragment

interface InitActivity

class InitActivityImpl : AppCompatActivity(), InitActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_main)
        initFragment(ActivityMainFragment())
    }

    private fun initFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.OptionFragment, fragment)
            .commit()
    }
}