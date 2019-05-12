package com.movieapp

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.Util.ApiInterface
import io.reactivex.Maybe
import io.reactivex.Observable
import org.junit.*
import org.junit.Assert.assertNotNull
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.net.SocketException
import org.mockito.Mockito.`when`
import android.text.method.TextKeyListener.clear
import com.nhaarman.mockitokotlin2.doThrow
import org.mockito.Mockito.`when`
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.`when`


@RunWith(JUnit4::class)
class VmMovieListTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var apiInterface: ApiInterface

    lateinit var vmMovieList: VmMovieList


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        this.vmMovieList = VmMovieList()
    }

    @Test
    fun fetchNowPlayingRepositories_positiveResponse() {
        Mockito.`when`(this.apiInterface.getNowPlayig(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Maybe.just(ArgumentMatchers.anyList<NowPlaying>())
        }

        val observer = mock(Observer::class.java) as Observer<NowPlaying>
        this.vmMovieList.nowPlayingAPIResult.observeForever(observer)

        this.vmMovieList.getNowPlayingInfo(ArgumentMatchers.anyString())

        assertNotNull(this.vmMovieList.nowPlayingAPIResult.value)
//        assertEquals(LiveDataResult.Status.SUCCESS, this.vmMovieList.nowPlayingAPIResult.value?.status)
    }



    @Test
    fun fetchNowPlayingRepositories_error() {
        Mockito.`when`(this.apiInterface.getNowPlayig(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Maybe.error<SocketException>(SocketException("No network here"))
        }

        //test the add functionality

        val observer = mock(Observer::class.java) as Observer<NowPlaying>
        this.vmMovieList.nowPlayingAPIResult.observeForever(observer)

        this.vmMovieList.getNowPlayingInfo(ArgumentMatchers.anyString())

        assertNotNull(this.vmMovieList.nowPlayingAPIResult.value)
//        Assert.assertEquals(LiveDataResult.Status.ERROR, this.vmMovieList.repositoriesLiveData.value?.status)
//        assert(this.vmMovieList.nowPlayingAPIResult is Throwable)


//        `when`(myMockedList.get(anyInt())).thenThrow(NullPointerException())
//        doThrow(RuntimeException()).`when`(myMockedList).clear()

    }



    @Test
    fun setLoadingNowPlayingVisibility_onSuccess() {
        Mockito.`when`(this.apiInterface.getNowPlayig(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())).thenAnswer {
            return@thenAnswer Maybe.just(listOf<NowPlaying>())
        }

        val spiedViewModel = com.nhaarman.mockitokotlin2.spy(this.vmMovieList)

        spiedViewModel.getNowPlayingInfo(ArgumentMatchers.anyString())
        Mockito.verify(spiedViewModel, Mockito.times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())
    }


    @Test
    fun setLoadingNowPlayingVisibility_onError() {
        Mockito.`when`(this.apiInterface.getNowPlayig(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())).thenAnswer {
            return@thenAnswer Maybe.error<SocketException>(SocketException())
        }

        val spiedViewModel = com.nhaarman.mockitokotlin2.spy(this.vmMovieList)

        spiedViewModel.getNowPlayingInfo(ArgumentMatchers.anyString())
        Mockito.verify(spiedViewModel, Mockito.times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())
    }

    @Test
    fun setLoadingVisibilityNowPlaying_onNoData() {
        Mockito.`when`(this.apiInterface.getNowPlayig(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any()  )).thenReturn(null)

        val spiedViewModel = com.nhaarman.mockitokotlin2.spy(this.vmMovieList)

        spiedViewModel.getNowPlayingInfo(ArgumentMatchers.anyString())
        Mockito.verify(spiedViewModel, Mockito.times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())
    }



    @Test
    fun fetchPopularRepositories_positiveResponse() {
        Mockito.`when`(this.apiInterface.getPopular(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Maybe.just(ArgumentMatchers.anyList<Popular>())
        }

        val observer = mock(Observer::class.java) as Observer<Popular>
        this.vmMovieList.popularAPIResult.observeForever(observer)

        this.vmMovieList.getPopularInfo(ArgumentMatchers.anyString())

        assertNotNull(this.vmMovieList.popularAPIResult.value)
//        assertEquals(LiveDataResult.Status.SUCCESS, this.vmMovieList.nowPlayingAPIResult.value?.status)
    }



    @Test
    fun fetchPopularRepositories_error() {
        Mockito.`when`(this.apiInterface.getPopular(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Maybe.error<RuntimeException>(RuntimeException("No network here"))
        }

        val observer = mock(Observer::class.java) as Observer<Popular>
        this.vmMovieList.popularAPIResult.observeForever(observer)

        this.vmMovieList.getPopularInfo(ArgumentMatchers.anyString())

        assertNotNull(this.vmMovieList.popularAPIResult.value)
//        Assert.assertEquals(LiveDataResult.Status.ERROR, this.vmMovieList.repositoriesLiveData.value?.status)
//        assert(this.vmMovieList.popularAPIResult is Throwable)
    }

    @Test
    fun setLoadingPopularVisibility_onSuccess() {
        Mockito.`when`(this.apiInterface.getPopular(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())).thenAnswer {
            return@thenAnswer Maybe.just(listOf<NowPlaying>())
        }

        val spiedViewModel = com.nhaarman.mockitokotlin2.spy(this.vmMovieList)

        spiedViewModel.getPopularInfo(ArgumentMatchers.anyString())
        Mockito.verify(spiedViewModel, Mockito.times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())
    }


    @Test
    fun setLoadingPopularVisibility_onError() {
        Mockito.`when`(this.apiInterface.getPopular(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())).thenAnswer {
            return@thenAnswer Maybe.error<SocketException>(SocketException())
        }

        val spiedViewModel = com.nhaarman.mockitokotlin2.spy(this.vmMovieList)

        spiedViewModel.getPopularInfo(ArgumentMatchers.anyString())
        Mockito.verify(spiedViewModel, Mockito.times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())
    }




    @Test
    fun setLoadingVisibilityPopular_onNoData() {
        Mockito.`when`(this.apiInterface.getPopular(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any()  )).thenReturn(null)

        val spiedViewModel = com.nhaarman.mockitokotlin2.spy(this.vmMovieList)

        spiedViewModel.getPopularInfo(ArgumentMatchers.anyString())
        Mockito.verify(spiedViewModel, Mockito.times(2)).setLoadingVisibility(ArgumentMatchers.anyBoolean())
    }
}