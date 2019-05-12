package com.movieapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_movie_list.view.*
import java.util.*

/**
 * Create adapter view to display movie list.
 * @context Holds activity context
 * @movieListItem Holds movie list
 * @clickListener Holds listener to handle click event on recycler view
 */
class AdapterMovieList (val context: Context, var movieListItem: ArrayList<MovieListItem>, val clickListener: (MovieListItem) -> Unit) : RecyclerView.Adapter<AdapterMovieList.ViewHolder>() {

    // Tag value to show AdapterMovieList name in log
    companion object {
        private val TAG = "AdapterMovieList"
    }

    //This method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterMovieList.ViewHolder {
        Log.v(TAG, String.format("onCreateViewHolder() :: parent, viewType "))
        var v = LayoutInflater.from(parent.context).inflate(R.layout.row_movie_list, parent, false)
        return ViewHolder(v)
    }

    //This method is binding the data on the list
    override fun onBindViewHolder(holder: AdapterMovieList.ViewHolder, position: Int) {
        Log.v(TAG, String.format("onBindViewHolder() :: holder, position = %d", position))
        (holder as ViewHolder).bindItems(movieListItem[position], clickListener)
    }

    // Get total item count
    override fun getItemCount() = movieListItem.size


    fun getMovieItemDtoList(): ArrayList<MovieListItem> {
        Log.v(TAG, String.format("getMovieItemDtoList()"))
        return movieListItem
    }

    //The class is holding the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(movieListItem: MovieListItem, clickListener: (MovieListItem) -> Unit) {

            var url: String = "https://image.tmdb.org/t/p/original/"+movieListItem.poster_path
            itemView.imgvPoster.setImageURI(url)

            Log.v(TAG, String.format("ViewHolder :: bindItems() :: movieListItem  title = %s, vote count = %d, url = %s", ""+movieListItem.title, movieListItem.vote_count, url))

            itemView.txtvTitle!!.text = movieListItem.title
            itemView.txtvRatingText!!.text = ""+ movieListItem.vote_average
            itemView.setOnClickListener { clickListener(movieListItem) }
        }
    }


    // Return movie item list
    fun getMovieItemList(): java.util.ArrayList<MovieListItem> {
        Log.v(TAG, "getMovieItemList()")
        return  movieListItem
    }

}