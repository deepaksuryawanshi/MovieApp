package com.movieapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_movie_list.view.*


/**
 * Create adapter view to display popular movie list.
 * @context Holds activity context
 * @movieListItem Holds movie list
 * @clickListener Holds listener to handle click event on recycler view
 */
class AdapterPopularList (val context: Context, val movieListItem: ArrayList<MovieListItem>, val clickListener: (MovieListItem) -> Unit) : RecyclerView.Adapter<AdapterPopularList.ViewHolder>() {

    // Tag value to show AdapterPopularList name in log
    companion object {
        private val TAG = "AdapterPopularList"
    }

    //This method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPopularList.ViewHolder {
        Log.v(TAG, String.format("onCreateViewHolder() :: parent, viewType "))
        var v = LayoutInflater.from(parent.context).inflate(R.layout.row_movie_list, parent, false)
        return ViewHolder(v)
    }

    //This method is binding the data on the list
    override fun onBindViewHolder(holder: AdapterPopularList.ViewHolder, position: Int) {
        Log.v(TAG, String.format("onBindViewHolder() :: holder, position = %d", position))
        (holder as ViewHolder).bindItems(movieListItem[position], clickListener)
    }

    // Get total item count
    override fun getItemCount() = movieListItem.size


    //The class is holding the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(movieListItem: MovieListItem, clickListener: (MovieListItem) -> Unit) {

            var url:String = "https://image.tmdb.org/t/p/original/"+movieListItem.poster_path

            itemView.imgvPoster.setImageURI(url)

            Log.v(TAG, String.format("ViewHolder :: bindItems() :: movieListItem  title = %s, vote count = %d, url = %s", ""+movieListItem.title, movieListItem.vote_count, url))

            itemView.txtvTitle!!.text = movieListItem.title
            itemView.txtvRatingText!!.text = ""+ movieListItem.vote_average
            itemView.setOnClickListener { clickListener(movieListItem) }
        }
    }
}