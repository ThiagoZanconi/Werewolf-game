package werewolf.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class InitActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(InitActivityImpl::class.java)

    @Test
    fun generalTest() {
        onView(withId(R.id.startButton)).check(matches(isDisplayed()))
        onView(withId(R.id.termEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.addButton)).check(matches(isDisplayed()))

        addPlayers()
        onView(withId(R.id.startButton)).perform(click())
    }

    private fun addPlayers(){
        repeat(9) { index ->
            addPlayer(index)
        }
    }

    private fun addPlayer(index: Int){
        onView(withId(R.id.termEditText)).perform(typeText(index.toString()), closeSoftKeyboard())
        onView(withId(R.id.addButton)).perform(click())
    }
}