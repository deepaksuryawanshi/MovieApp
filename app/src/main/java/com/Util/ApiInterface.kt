package com.Util

import com.model.Search
import com.movieapp.MovieDetail
import com.movieapp.NowPlaying
import com.movieapp.Popular
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiInterface {

    @GET("now_playing?")
    fun getNowPlayig(@Query("api_key") api_key: String, @Query("language") lang: String, @Query("page") page: String): io.reactivex.Observable<NowPlaying>

    @GET("popular?")
    fun getPopular(@Query("api_key") api_key: String, @Query("language") lang: String, @Query("page") page: String): io.reactivex.Observable<Popular>

    @GET()
    fun getMovieDetail(@Url movieId: String, @Query("api_key") api_key: String, @Query("language") lang: String): io.reactivex.Observable<MovieDetail>

    @GET("search/movie?")
    fun getMovieSearch(@Query("api_key") api_key: String, @Query("query") query: String, @Query("page") page: String): io.reactivex.Observable<Search>

}