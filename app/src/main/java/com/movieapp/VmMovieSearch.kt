package com.movieapp

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.Util.ApiClient
import com.model.Search
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *  View Model class to executes api for movie search screen.
 */
class VmMovieSearch : ViewModel(){

    // Holds movie detail live data reference.
    val searchAPIResult = MediatorLiveData<Search>()

    // Holds api key reference
    private var KEY: String = AppController.KEY

    val apiClient by lazy {
        ApiClient.createSearch()
    }

    var mCompositeDisposable: CompositeDisposable = CompositeDisposable()


    fun getMovieSearchResult(searchString: String, pageNo: String) {

        //    @GET("now_playing?api_key=5bd344d4c7687904cc86d08fb33bbc65&language=en-US&page=1")
        mCompositeDisposable!!.add(apiClient.getMovieSearch(KEY, searchString, pageNo)
                .observeOn(AndroidSchedulers.mainThread()) // specifies the scheduler on which an observer will observe this Observable
                .subscribeOn(Schedulers.io()) // tells the source Observable which thread to use for emitting items to the Observer
                .subscribe(this::movieSearchAPIResultSuccess, this::handleError))
    }

    private fun movieSearchAPIResultSuccess(movieSearch: Search) {

        var userLiveDataNetwork: MutableLiveData<Search> = MutableLiveData()
        userLiveDataNetwork.value = movieSearch

        searchAPIResult.addSource(userLiveDataNetwork, object : Observer<Search> {
            override fun onChanged(t: Search?) {
                searchAPIResult.setValue(t)
                searchAPIResult.removeSource(userLiveDataNetwork)
            }
        })
    }

    private fun handleError(throwable: Throwable) {
        Log.v("ViewModelLogin", "error")
    }

}