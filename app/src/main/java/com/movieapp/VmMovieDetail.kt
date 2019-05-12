package com.movieapp

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.Util.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *  View Model class to executes api for movie detail screen.
 */
class VmMovieDetail : ViewModel() {

    // Holds movie detail live data reference.
    val movieDetailAPIResult = MediatorLiveData<MovieDetail>()

    // Holds api key reference
    private var KEY: String = AppController.KEY

    // Holds reference for api client class
    val apiClient by lazy {
        ApiClient.create()
    }

    // Holds reference for disposable components
    var mCompositeDisposable: CompositeDisposable = CompositeDisposable()


    /**
     * Load movie data from server.
     * @id movie id
     */
    fun getMovieDetailInfo(id: String) {

        mCompositeDisposable!!.add(apiClient.getMovieDetail(id, KEY, "en-US")
                .observeOn(AndroidSchedulers.mainThread()) // specifies the scheduler on which an observer will observe this Observable
                .subscribeOn(Schedulers.io()) // tells the source Observable which thread to use for emitting items to the Observer
                .subscribe(this::movieDetailAPIResultSuccess, this::handleError))
    }

    /**
     * Handle movie detail api result.
     * @movieDetail movie detail api response.
     */
    private fun movieDetailAPIResultSuccess(movieDetail: MovieDetail) {

        var userLiveDataNetwork: MutableLiveData<MovieDetail> = MutableLiveData()
        userLiveDataNetwork.value = movieDetail

        movieDetailAPIResult.addSource(userLiveDataNetwork, object : Observer<MovieDetail> {
            override fun onChanged(t: MovieDetail?) {
                movieDetailAPIResult.setValue(t)
                movieDetailAPIResult.removeSource(userLiveDataNetwork)
            }
        })

    }

    /**
     * Handle api error response.
     * @throwable error to be thrown
     */
    private fun handleError(throwable: Throwable) {
        Log.v("ViewModelLogin", "error")
    }
}