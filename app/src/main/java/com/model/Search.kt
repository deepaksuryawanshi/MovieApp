package com.model

data class Search(
    val page: Int,
    val results: ArrayList<ResultSearch>,
    val total_pages: Int,
    val total_results: Int
)

data class ResultSearch(
    val adult: Boolean,
    val backdrop_path: Any,
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
)