package com.movieapp

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.movie_detail.*
import org.koin.android.ext.android.inject


/**
 * Movie detail activity to show movie details.
 */
class ActivityMovieDetail : BaseActivity() {

    // ViewModel instance created for class VmMovieDetail
    private val vmMovieDetail: VmMovieDetail by inject()

    // Tag value to show activity name in log
    private val TAG = ActivityMovieDetail::class.java!!.getSimpleName()

    // Identifies movie id
    var id: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v(TAG, "onCreate()")
        inItUI()

    }

    /**
     * Initialize UI.
     */
    private fun inItUI() {
        Log.v(TAG, "inItUI()")

        // Check bundle extra is not null
        if (intent.extras != null) {
            id = intent.extras.getInt("movieId")

            if (id != 0) {
                // Check network availability
                if (isNetworkAvailable()) {
                    Log.v(TAG, "inItUI() :: Network available")
                    inItUIMovieDetail(id)
                    observeViewModelMovieDetail()
                } else {
                    Log.v(TAG, "inItUI() :: Network not available")
                    showToast(getString(R.string.internet_not_available))
                }
            }
        }
    }


    /**
     * Load movie detail from server.
     * Register observer to receive data change.
     */
    private fun inItUIMovieDetail(id: Int) {
        Log.v(TAG, String.format("inItUIMovieDetail() :: movieId = %d", id))
        showProgressDialog()
        vmMovieDetail.getMovieDetailInfo(id.toString())
    }


    /**
     * Observe movide detail data change from server.
     */
    private fun observeViewModelMovieDetail() {
        Log.v(TAG, "observeViewModelMovieDetail()")
        vmMovieDetail.movieDetailAPIResult.observe(this, Observer<MovieDetail> { result ->
            if (result != null) {
                Log.v(TAG, String.format("observeViewModelMovieDetail() :: result not null"))
                setMovieData(result)
            }
        })
    }


    // Set movie data to UI
    private fun setMovieData(result: MovieDetail) {
        Log.v(TAG, String.format("setMovieData() :: result id = %d,  title = %s, vote count = %d", result.id, result.title, result.vote_count))

        tvTitle.text = result!!.title
        tvRunningTimeText.text = result!!.runtime.toString()
        tvReleaseDateText.text = result!!.release_date

        var genres: String = ""

        for (genre in result!!.genres) {
            if (genres.equals(""))
                genres = genre.name
            else
                genres = genres + ", " + genre.name
        }

        tvGenresText.text = genres
        tvVotesText.text = result!!.vote_count.toString()
        tvRatingText.text = result!!.vote_average.toString()
        tvSynopsisText.text = result!!.overview

        // Create image url
        var url: String = "https://image.tmdb.org/t/p/original" + result!!.poster_path!!

        // Load image from server
        var imageLoadingUtility: ImageLoadingUtility = ImageLoadingUtility()
        imageLoadingUtility.loadImage(imgvMoviePoster, url, applicationContext, View.OnClickListener { })

//        imgvMoviePoster.setImageURI(url)

        // Dismiss progress dialog
        dismissProgressDialog()
    }




    override fun getLayoutResource(): Int {
        Log.v(TAG, "getLayoutResource()")
        return R.layout.movie_detail
    }

    override fun getToolbarTitle(): String? {
        Log.v(TAG, "getToolbarTitle() :: Movie Detail")
        return "Movie Detail"
    }
}