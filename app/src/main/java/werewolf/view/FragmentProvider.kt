package werewolf.view

import androidx.fragment.app.Fragment
import com.example.observer.Subject
import org.json.JSONObject
import werewolf.view.fragments.rolefragments.ArsonistFragment
import werewolf.view.fragments.rolefragments.DetectiveFragment
import werewolf.view.fragments.rolefragments.DetectiveGridFragment
import werewolf.view.fragments.PlayerFragment
import werewolf.view.fragments.PlayerGridFragment
import werewolf.view.fragments.rolefragments.StalkerFragment
import werewolf.view.fragments.rolefragments.VeteranFragment
import werewolf.view.fragments.rolefragments.WerewolfPlayerFragment

enum class FragmentEnum{
    ArsonistFragment, DetectiveFragment, DetectiveGridFragment, StalkerFragment, VeteranFragment, PlayerGridFragment, WerewolfPlayerFragment, PlayerFragment
}

object FragmentProvider {

    fun getFragment(onActionSubject: Subject<GameUiEventSignal>, jsonObject: JSONObject): Fragment {
        val fragmentEnum = FragmentEnum.valueOf(jsonObject.getString("Fragment"))
        return when(fragmentEnum){
            FragmentEnum.ArsonistFragment -> ArsonistFragment(onActionSubject, jsonObject)
            FragmentEnum.DetectiveFragment -> DetectiveFragment(onActionSubject, jsonObject)
            FragmentEnum.StalkerFragment -> StalkerFragment(onActionSubject, jsonObject)
            FragmentEnum.VeteranFragment -> VeteranFragment(onActionSubject, jsonObject)
            FragmentEnum.PlayerGridFragment -> PlayerGridFragment(onActionSubject, jsonObject)
            FragmentEnum.WerewolfPlayerFragment -> WerewolfPlayerFragment(onActionSubject, jsonObject)
            FragmentEnum.DetectiveGridFragment -> DetectiveGridFragment(onActionSubject, jsonObject)
            FragmentEnum.PlayerFragment -> PlayerFragment(onActionSubject, jsonObject)
        }
    }
}