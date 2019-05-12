package com.movieapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class NowPlaying(
        val dates: Dates,
        val page: Int,
        val results: ArrayList<MovieListItem>,
        val total_pages: Int,
        val total_results: Int
)

data class Dates(
    val maximum: String,
    val minimum: String
)

@Parcelize
data class MovieListItem(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: ArrayList<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
): Parcelable