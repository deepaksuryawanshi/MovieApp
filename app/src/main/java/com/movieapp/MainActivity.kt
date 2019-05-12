package com.movieapp

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.test.espresso.IdlingResource
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import java.util.*


/**
 * Main activity to show Playing and Popular movies list.
 */
class MainActivity : BaseActivity(), ShowProgress {

    // ViewModel instance created for class VmMovieList
    private val vmMovieList: VmMovieList by inject()

    // Tag value to show activity name in log
    private val TAG = MainActivity::class.java!!.getSimpleName()

    // Page number to load data now playing movies from server
    var pageNoNowPlaying: Int = 1

    // Page number to load data popular movies from server
    var pageNoPopular: Int = 1

    // Collection to hold now playing movie list
    var movieItemList = ArrayList<MovieListItem>()

    // Collection to hold popular movie list
    var popularItemList = ArrayList<MovieListItem>()

    // The Idling Resource which will be null in production.
    var mIdlingResource: MovieListIdlingResource = MovieListIdlingResource()

     // Menu item id popular.
    var MENU_POPULAR: Int = 1

     // Menu item id now playing.
    var MENU_NOW_PLAYING: Int = 2

    // Menu item search
    var MENU_SEARCH: Int = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate()")

        inItUI()

    }

    /**
     * Initialize UI.
     */
    private fun inItUI(){
        Log.v(TAG, "inItUI()")

         // Check network availability.
        if(isNetworkAvailable()) {
            Log.v(TAG, "inItUI() :: Network available")

            showProgressDialog()

            // Initialize UI for popular and now playing recycler view
            inItUINowPlaying()
            inItUINowPopular()

            // Register observer for popular and now playing movies
            observeViewModelNowPlaying()
            observeViewModelPopular()
        } else {
            Log.v(TAG, "inItUI() :: Network not available")
            // Shows network not available toast
            showToast(getString(R.string.internet_not_available))
        }
    }


    /**
     * Load playing movies data from server.
     * Register observer to receive data change.
     */
    private fun inItUINowPlaying() {
        Log.v(TAG, "inItUINowPlaying()")

        // Set playing movies list visible
        setNowPlayingListVisible()

        /**
         * Load playing movies data from server.
         * @pageNoNowPlaying page number to load data from server.
         */
        vmMovieList.getNowPlayingInfo(pageNoNowPlaying.toString())

        // Set recycler view orientation
        rvMovieList.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        var directionDownNowPlaying: Boolean = false;

        // Added on scroll listener to now playing movies recyclerView.
        rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.v(TAG, "inItUINowPlaying():: onScrolled()")

                directionDownNowPlaying = dy > 0
                if ((!recyclerView!!.canScrollVertically(1) && directionDownNowPlaying)) {
                    directionDownNowPlaying = false
                    showProgressDialog()
                    vmMovieList.getNowPlayingInfo((pageNoNowPlaying).toString())
                }
            }
        })
    }


    /**
     * Load popular movies data from server.
     * Register observer to receive data change.
     */
    private fun inItUINowPopular() {
        Log.v(TAG, "inItUINowPopular()")

        /**
         * Load popular movies data from server.
         * @pageNoPopular page number to load data from server.
         */
        vmMovieList.getPopularInfo(pageNoPopular.toString())

        // Set recycler view orientation
        rvPopular.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        var directionDownPopular: Boolean = false;

        // Added on scroll listener to popular movies recyclerView.
        rvPopular.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.v(TAG, "inItUINowPopular():: onScrolled()")

                directionDownPopular = dy > 0
                if ((!recyclerView!!.canScrollVertically(1) && directionDownPopular)) {
                    directionDownPopular = false
                    showProgressDialog()
                    vmMovieList.getPopularInfo((pageNoPopular).toString())
                }
            }
        })
    }


    /**
     * Observe playing movies data change from server.
     */
    private fun observeViewModelNowPlaying() {
        Log.v(TAG, "observeViewModelNowPlaying()")

        vmMovieList.nowPlayingAPIResult.observe(this, Observer<NowPlaying> { result ->

            Log.v(TAG, "observeViewModelNowPlaying() :: observe()")
            dismissProgressDialog()
            handleNowPlayingResult(result)
        })
    }

    /**
     * Observe popular movies data change from server.
     */
    private fun observeViewModelPopular() {
        Log.v(TAG, "observeViewModelPopular()")

        vmMovieList.popularAPIResult.observe(this, Observer<Popular> { result ->
            Log.v(TAG, "observeViewModelPopular() :: observe()")

            dismissProgressDialog()
            handlePopularAPIResult(result)
        })
    }


    /**
     * Handle playing movies result from server.
     * @nowPlaying model receives newly arrived data.
     */
    private fun handleNowPlayingResult(nowPlaying: NowPlaying?) {
        Log.v(TAG, "handleNowPlayingResult()")

        // Check received data size and increment page number by one to load next page data
        if (nowPlaying!!.results.size > 0) {
            Log.v(TAG, String.format("handleNowPlayingResult() :: nowPlaying size = %d", nowPlaying!!.results.size))
            movieItemList.addAll(nowPlaying!!.results)
            pageNoNowPlaying = pageNoNowPlaying + 1
        }

        // Set updated data to now playing movies recyclerview
        if (rvMovieList.adapter == null && movieItemList.size > 0) {
            Log.v(TAG, String.format("handleNowPlayingResult() :: movieItemList size = %d", movieItemList.size))
            rvMovieList.adapter = AdapterMovieList(this, movieItemList, { partItem: MovieListItem -> partItemClicked(partItem) })
        } else {
            Log.v(TAG, "handleNowPlayingResult() :: notifyDataSetChanged()")
            rvMovieList.adapter!!.notifyDataSetChanged()
        }

    }


    /**
     * Handle recycler view item click event.
     * @movieListItem data set used to create recycler view item.
     */
    private fun partItemClicked(movieListItem: MovieListItem) {
        Log.v(TAG, String.format("handleNowPlayingResult() :: partItemClicked() :: id = %d,  title = %s, vote count = %d", movieListItem.id, movieListItem.title, movieListItem.vote_count))

        // Launch second activity, pass part ID as string parameter
        val showDetailActivityIntent = Intent(this, ActivityMovieDetail::class.java)
        showDetailActivityIntent.putExtra("movieId", movieListItem.id)
        startActivity(showDetailActivityIntent)
    }


    /**
     * Handle popular movies result from server.
     * @popular model receives newly arrived data.
     */
    private fun handlePopularAPIResult(popular: Popular?) {
        Log.v(TAG, "handlePopularAPIResult()")

        // Check received data size and increment page number by one to load next page data
        if (popular!!.results.size > 0) {
            Log.v(TAG, String.format("handlePopularAPIResult() :: popular size = %d", popular!!.results.size))
            popularItemList.addAll(popular!!.results)
            pageNoPopular = pageNoPopular + 1
        }

        // Set updated data to popular movies recycler view
        if (rvPopular.adapter == null && popularItemList.size > 0) {
            Log.v(TAG, String.format("handlePopularAPIResult() :: popularItemList size = %d", popularItemList.size))
            rvPopular.adapter = AdapterPopularList(this, popularItemList, { partItem: MovieListItem -> partItemClicked(partItem) })
        } else {
            Log.v(TAG, "handlePopularAPIResult() :: notifyDataSetChanged()")
            rvPopular.adapter!!.notifyDataSetChanged()
        }

    }



    override fun onPrepareOptionsMenu(menu: Menu): Boolean {

        menu.clear();
        menu.add(0, MENU_NOW_PLAYING, Menu.NONE, "NOW PLAYING").setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
        menu.add(0, MENU_POPULAR, Menu.NONE, "POPULAR").setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
        menu.add(0, MENU_SEARCH, Menu.NONE, "SEARCH").setIcon(R.drawable.ic_search_white).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MENU_NOW_PLAYING -> {
                Log.v(TAG, "onOptionsItemSelected() :: MENU_NOW_PLAYING menu item clicked")
                setNowPlayingListVisible()
            }
            MENU_POPULAR -> {
                Log.v(TAG, "onOptionsItemSelected() :: MENU_POPULAR menu item clicked")
                setPopularListVisible()
            }
            MENU_SEARCH -> {
                Log.v(TAG, "onOptionsItemSelected() :: MENU_SEARCH menu item clicked")
                startActivity(Intent(this, ActivitySearch::class.java))
            }
        }
        return false
    }

    /**
     * Switch between playing and popular movies list
     * Set playing movies list visible and popular list invisible
     */
    private fun setNowPlayingListVisible() {
        Log.v(TAG, "setNowPlayingListVisible() :: Playing movies list visible")
        rvMovieList.visibility = View.VISIBLE
        rvPopular.visibility = View.GONE
    }


    /**
     * Switch between playing and popular movies list
     * Set popular movies list visible and playing list invisible
     */
    private fun setPopularListVisible() {
        Log.v(TAG, "setPopularListVisible() :: Playing movies list visible")
        rvMovieList.visibility = View.GONE
        rvPopular.visibility = View.VISIBLE
    }


    /**
     * Get playing movies list.
     */
    fun getMovieItemDtoList(): ArrayList<MovieListItem> {
        Log.v(TAG, "getMovieItemDtoList()")
        return movieItemList
    }


    override fun getLayoutResource(): Int {
        Log.v(TAG, "getLayoutResource()")
        return R.layout.activity_main
    }

    override fun getToolbarTitle(): String? {
        Log.v(TAG, "getToolbarTitle() :: Movie List")
        return "Movie List"
    }


    /**
     * Only called from test, creates and returns a new [MovieListIdlingResource].
     */
    @VisibleForTesting
    fun getIdlingResource(): IdlingResource {
        Log.v(TAG, "getIdlingResource()")

        if (mIdlingResource == null) {
            mIdlingResource = MovieListIdlingResource()
        }
        return mIdlingResource
    }


    fun setIdle(b: Boolean) {
        Log.v(TAG, String.format("setIdle() :: b = %b", b))

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(b)
        }
    }

    override fun onProgressShown(b: Boolean) {
        Log.v(TAG, String.format("onProgressShown() :: b = %b", b))
    }

}
