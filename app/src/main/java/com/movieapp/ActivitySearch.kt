package com.movieapp

import android.os.Bundle
import android.util.Log
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.Adapter.AdapterSearchResult
import com.model.ResultSearch
import com.model.Search
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.movie_detail.*
import org.koin.android.ext.android.inject

/**
 * Search activity to show search result.
 */
class ActivitySearch  : BaseActivity() {

    // Tag value to show activity name in log
    private val TAG = ActivitySearch::class.java!!.getSimpleName()

    // ViewModel instance created for class VmMovieSearch
    private val vmMovieSearch: VmMovieSearch by inject()

    var searchString : String = ""

    var resultSearch = ArrayList<ResultSearch>()

    // Page number to load data for searched movies from server
    var pageNoSearchResult: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inItUI()
    }


    /**
     * Initialize UI.
     */
    private fun inItUI(){

        // Check network availability
        if (isNetworkAvailable()) {
            Log.v(TAG, "inItUI() :: Network available")
            inItUIMovieSearch()
            observeViewModelMovieSearch()
        } else {
            Log.v(TAG, "inItUI() :: Network not available")
            showToast(getString(R.string.internet_not_available))
        }


        etSearch.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(200))

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {
                if(!p0!!.isEmpty()){
//                    imgvClose.visibility = View.VISIBLE
                    searchString = p0.toString()
                    pageNoSearchResult = 1
                    resultSearch.clear()
                    vmMovieSearch.getMovieSearchResult(searchString, pageNoSearchResult.toString())
                } else {
                    searchString = ""
//                    vmMovieSearch.getMovieSearchResult("us")
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }

    /**
     * Observe movie detail data change from server.
     */
    private fun observeViewModelMovieSearch() {
        Log.v(TAG, "observeViewModelMovieDetail()")
        vmMovieSearch.searchAPIResult.observe(this, Observer<Search> { result ->
            if (result != null) {
                Log.v(TAG, String.format("observeViewModelMovieDetail() :: result not null"))
                setSearchResultData(result)
            }
        })
    }

    // Set movie data to UI
    private fun setSearchResultData(searchResult: Search) {
        Log.v(TAG, String.format("setSearchResultData() :: result"))

        // Set recycler view orientation
        rvSearch.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        var directionDownPopular: Boolean = false;

        // Added on scroll listener to popular movies recyclerView.
        rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.v(TAG, "inItUINowPopular():: onScrolled()")

                directionDownPopular = dy > 0
                if (!recyclerView!!.canScrollVertically(1) && directionDownPopular) {
                    directionDownPopular = false
                    showProgressDialog()

                    vmMovieSearch.getMovieSearchResult(searchString, pageNoSearchResult.toString())
                }
        }
        })

        // Check received data size and increment page number by one to load next page data
        if (searchResult.results.size > 0) {
            Log.v(TAG, String.format("handlePopularAPIResult() :: popular size = %d", searchResult.results.size))
            resultSearch.addAll(searchResult.results)

            Toast.makeText(applicationContext, "Page: "+pageNoSearchResult, Toast.LENGTH_SHORT).show()
            pageNoSearchResult = pageNoSearchResult + 1

        }

        // Set updated data to now playing movies recyclerview
        if (rvSearch.adapter == null && resultSearch.size > 0) {
            Log.v(TAG, String.format("handleNowPlayingResult() :: movieItemList size = %d", searchResult.results.size))
            rvSearch.adapter = AdapterSearchResult(this, resultSearch, { partItem: ResultSearch -> partItemClicked(partItem) })
        }

        else {
            Log.v(TAG, "handleNowPlayingResult() :: notifyDataSetChanged()")
            rvSearch.adapter!!.notifyDataSetChanged()
            if(resultSearch.size > 3)
            rvSearch.scrollToPosition(resultSearch.size -3)
        }


        // Dismiss progress dialog
        dismissProgressDialog()
    }


    /**
     * Handle recycler view item click event.
     * @movieListItem data set used to create recycler view item.
     */
    private fun partItemClicked(movieListItem: ResultSearch) {
        Log.v(TAG, String.format("handleNowPlayingResult() :: partItemClicked() :: id = %d,  title = %s, vote count = %d", movieListItem.id, movieListItem.title, movieListItem.vote_count))

        // Launch second activity, pass part ID as string parameter
        val showDetailActivityIntent = Intent(this, ActivityMovieDetail::class.java)
        showDetailActivityIntent.putExtra("movieId", movieListItem.id)
        startActivity(showDetailActivityIntent)
    }


    /**
     * Load movie detail from server.
     * Register observer to receive data change.
     */
    private fun inItUIMovieSearch() {
        Log.v(TAG, String.format("inItUIMovieDetail() ::"))
    }


    override fun getToolbarTitle(): String? {

        Log.v(TAG, "getToolbarTitle() :: Search")
        return "Search"
    }

    override fun getLayoutResource(): Int {

        Log.v(TAG, "getLayoutResource()")
        return R.layout.activity_search

    }

}