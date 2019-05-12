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
 * View Model class to executes api for movie list screen.
 */
class VmMovieList : ViewModel() {

    // Holds reference for now playing movie list
    val nowPlayingAPIResult = MediatorLiveData<NowPlaying>()

    // Holds reference for now popular movie list
    val popularAPIResult = MediatorLiveData<Popular>()

    // Holds reference for now playing movie list
    val loadingLiveDataNowPlaying = MutableLiveData<Boolean>()

    // API key used to fetch data from server
    private var KEY: String = AppController.KEY

    // Holds reference for api client class
    val apiClient by lazy {
        ApiClient.create()
    }

    // Holds reference for disposable components
    var mCompositeDisposable: CompositeDisposable = CompositeDisposable()


    /**
     * Load now playing movie list from server.
     * @pageNo page number to load data sequentially on scroll of recycler view
     */
    fun getNowPlayingInfo(pageNo: String) {

        this.setLoadingVisibility(true)
        mCompositeDisposable!!.add(apiClient.getNowPlayig(KEY, "en-US", pageNo)
                .observeOn(AndroidSchedulers.mainThread()) // specifies the scheduler on which an observer will observe this Observable
                .subscribeOn(Schedulers.io()) // tells the source Observable which thread to use for emitting items to the Observer
                .subscribe(this::nowPlayingAPIResultSuccess, this::handleError))
    }

    /**
     * Load popular movie list from server.
     * @pageNo page number to load data sequentially on scroll of recycler view
     */
    fun getPopularInfo(pageNo: String) {

        this.setLoadingVisibility(true)
        mCompositeDisposable!!.add(apiClient.getPopular(KEY, "en-US", pageNo)
                .observeOn(AndroidSchedulers.mainThread()) // specifies the scheduler on which an observer will observe this Observable
                .subscribeOn(Schedulers.io()) // tells the source Observable which thread to use for emitting items to the Observer
                .subscribe(this::popularAPIResultSuccess, this::handleError))
    }


    /**
     * Handle now playing movies api result.
     * @nowPlaying now playing movie list.
     */
    private fun nowPlayingAPIResultSuccess(nowPlaying: NowPlaying) {

        this.setLoadingVisibility(true)

        var userLiveDataNetwork: MutableLiveData<NowPlaying> = MutableLiveData()
        userLiveDataNetwork.value = nowPlaying

        nowPlayingAPIResult.addSource(userLiveDataNetwork, object : Observer<NowPlaying> {
            override fun onChanged(t: NowPlaying?) {
                nowPlayingAPIResult.setValue(t)
                nowPlayingAPIResult.removeSource(userLiveDataNetwork)
            }
        })

    }


    /**
     * Handle popular movies api result.
     * @nowPlaying popular movie list.
     */
    private fun popularAPIResultSuccess(popular: Popular) {

        this.setLoadingVisibility(true)

        var userLiveDataNetwork: MutableLiveData<Popular> = MutableLiveData()
        userLiveDataNetwork.value = popular

        popularAPIResult.addSource(userLiveDataNetwork, object : Observer<Popular> {
            override fun onChanged(t: Popular?) {
                popularAPIResult.setValue(t)
                popularAPIResult.removeSource(userLiveDataNetwork)
            }
        })

    }


    /**
     * Handle api error response.
     * @throwable error to be thrown
     */
    private fun handleError(throwable: Throwable) {
        this.setLoadingVisibility(false)
        Log.v("ViewModelLogin", "error")
    }


    /**
     * Set a progress dialog visible on the View
     * @param visible visible or not visible
     */
    fun setLoadingVisibility(visible: Boolean) {
        loadingLiveDataNowPlaying.postValue(visible)
    }
}