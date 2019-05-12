package com.movieapp

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.*
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeUp
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.util.HumanReadables
import android.support.test.espresso.util.TreeIterables
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage.RESUMED
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.android.synthetic.main.activity_main.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @JvmField
    @Rule
    val mActivityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    var rvMovieList: RecyclerView? = null

    var movieList: ArrayList<MovieListItem> = ArrayList<MovieListItem>()

    var count: Int = 0

    internal var j = 0

    private var mIdlingResource: IdlingResource? = null


    @Before
    fun setUp() {

        Thread.sleep(10000)
//        onView(withId(R.id.rvMovieList)).check(matches(isDisplayed()))

        rvMovieList = getActivityInstance()!!.findViewById(R.id.rvMovieList)

        if (rvMovieList != null && rvMovieList!!.adapter != null) {
            count = rvMovieList!!.getAdapter()!!.getItemCount()

            movieList = mActivityRule.activity.getMovieItemDtoList()
        }
    }


    @Test
    fun checkVisibility() {
        onView(withId(R.id.rvMovieList)).check(matches(isDisplayed()));
        onView(withId(R.id.rvPopular)).check(matches(not(isDisplayed())));
    }


    @Test
    fun checkMovieListMenuItemVisibility() {

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext())
        onView(withText("NOW PLAYING")).perform(click())
        onView(withId(R.id.rvMovieList)).check(matches(isDisplayed()));
        onView(withId(R.id.rvPopular)).check(matches(not(isDisplayed())));
    }


    @Test
    fun scrollToLastPosition() {

        onView(withId(R.id.rvMovieList)).perform(scrollToPosition<AdapterMovieList.ViewHolder>(getActivityInstance()!!.rvMovieList!!.getAdapter()!!.getItemCount() - 1))

        swipeUp()
    }


    @Test
    fun checkPopularMenuItemVisibility() {

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext())
        onView(withText("POPULAR")).perform(click())
        onView(withId(R.id.rvPopular)).check(matches(isDisplayed()))
        onView(withId(R.id.rvMovieList)).check(matches(not(isDisplayed())))
    }


    @Test
    fun checkSearchMenuItemClick(){

        onView(withContentDescription("SEARCH")).perform(click())

        Thread.sleep(5000)

        val activity = getActivityInstance()
        val b = activity is ActivitySearch
        assertTrue(b)

//        Thread.sleep(5000)
        Espresso.pressBack()
    }

    @Test
    fun testListItemClick0() {

        Thread.sleep(10000)

        for (i in 0 until count) {
            testListItemClick(i)
        }
    }


    @Test
    fun testListItemData() {

        for (j in 0 until movieList.size) {

            onView(withId(R.id.rvMovieList)).perform(scrollToPosition<AdapterMovieList.ViewHolder>(j))

            var movieListItem = movieList.get(j)

            val recyclerViewMatcher = withRecyclerView(R.id.rvMovieList)

            onView(recyclerViewMatcher.atPositionOnView(j, R.id.txtvTitle)).check(matches(withText(movieListItem.title)))
            onView(recyclerViewMatcher.atPositionOnView(j, R.id.txtvRatingText)).check(matches(withText(movieListItem.vote_average.toString())))
//            onView(recyclerViewMatcher.atPositionOnView(i, R.id.txtvTitle)).check(matches(withText(movieListItem.title)))

        }

    }

    fun testListItemClick(i: Int) {

//        Thread.sleep(5000)

        onView(withId(R.id.rvMovieList)).perform(scrollToPosition<AdapterMovieList.ViewHolder>(i))
        onView(withId(R.id.rvMovieList)).perform(RecyclerViewActions.actionOnItemAtPosition<AdapterMovieList.ViewHolder>(i, SearchViewAction.clickChildViewWithId(R.id.row)))

        Thread.sleep(5000)

        val activity = getActivityInstance()
        val b = activity is ActivityMovieDetail
        assertTrue(b)

//        Thread.sleep(5000)
        Espresso.pressBack()
    }


    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    fun getActivityInstance(): Activity? {

        val activity = arrayOfNulls<Activity>(1)
        getInstrumentation().runOnMainSync {
            var currentActivity: Activity? = null
            val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED)
            if (resumedActivities.iterator().hasNext()) {
                currentActivity = resumedActivities.iterator().next() as Activity
                activity[0] = currentActivity
            }
        }

        return activity[0]
    }


    internal object SearchViewAction {

        fun clickChildViewWithId(id: Int): ViewAction {

            return object : ViewAction {
                override fun getConstraints(): Matcher<View>? {
                    return null
                }

                override fun getDescription(): String {
                    return "Click on a child view with specified id."
                }

                override fun perform(uiController: UiController, view: View) {
                    var v: View = view.findViewById<TextView>(id)
                    view.performClick()
                }
            }
        }
    }


    @Before
    fun registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource()
        // To prove that the test fails, omit this call:
        //        Espresso.registerIdlingResources(mIdlingResource);
        IdlingRegistry.getInstance().register(mIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            //            Espresso.unregisterIdlingResources(mIdlingResource);
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }


    /** Perform action of waiting for a specific view id.  */
    fun waitId(viewId: Int, millis: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for a specific view with id <$viewId> during $millis millis."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + millis
                val viewMatcher = withId(viewId)

                do {
                    for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)

                // timeout happens
                throw PerformException.Builder()
                        .withActionDescription(this.description)
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(TimeoutException())
                        .build()
            }
        }
    }


    /**
     * Perform action of waiting for a specific time.
     */
    public fun waitFor(millis: Long): ViewAction {


        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "Wait for $millis milliseconds."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadForAtLeast(millis)
            }
        }


    }


    fun withIndex(matcher: Matcher<View>, index: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            internal var currentIndex = 0

            override fun describeTo(description: Description) {
                description.appendText("with index: ")
                description.appendValue(index)
                matcher.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                return matcher.matches(view) && currentIndex++ == index
            }
        }
    }


    @Test
    fun netAvailable0() {

        val waitingTime: Long = 60000

        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(waitingTime * 2, TimeUnit.MILLISECONDS)
        IdlingPolicies.setIdlingResourceTimeout(waitingTime * 2, TimeUnit.MILLISECONDS)

        //        setWiFiOnOff(false);
        // Now we wait
        val idlingResource = WifiTimeIdlingResource(false)
        IdlingRegistry.getInstance().register(idlingResource)

        Thread.sleep(10000)
        assertFalse(isConnected(getActivityInstance()))

        // Clean up
        IdlingRegistry.getInstance().unregister(idlingResource)
    }


    private fun isConnected(context: Context?): Boolean {
        val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    @Test
    fun netAvailable1() {

        val waitingTime: Long = 80000

        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(waitingTime * 2, TimeUnit.MILLISECONDS)
        IdlingPolicies.setIdlingResourceTimeout(waitingTime * 2, TimeUnit.MILLISECONDS)

        //        setWiFiOnOff(true);
        // Now we wait
        val idlingResource = WifiTimeIdlingResource(true)
        IdlingRegistry.getInstance().register(idlingResource)

        Thread.sleep(20000)
        assertTrue(isConnected(getActivityInstance()))

        // Clean up
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}