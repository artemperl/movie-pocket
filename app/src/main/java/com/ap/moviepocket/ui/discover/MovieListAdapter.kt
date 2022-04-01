package com.ap.moviepocket.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.*
import com.ap.model.Movie
import com.ap.moviepocket.databinding.ListItemDiscoverMovieBinding
import com.ap.moviepocket.ui.discover.MovieListAdapter.MovieDataItem
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.LinkedHashMap

class MovieListAdapter :
    ListAdapter<MovieDataItem, MovieListAdapter.MovieViewHolder>(MovieDiffCallback()) {

    var bottomSpace = 0
    private val movieMap = LinkedHashMap<String, List<Movie>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ListItemDiscoverMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    inner class MovieViewHolder(
        private val binding: ListItemDiscoverMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MovieDataItem) {
            when (item) {
                is MovieDataItem.MovieItem -> binding.apply {
                    heading.visibility = View.GONE
                    movieContainer.visibility = View.VISIBLE
                    loadMoreButton.visibility = View.GONE
                    movie = item.movie
                    Picasso.get().load(item.movie.posterUrl).into(movieItemImage)
                    executePendingBindings()
                }
                is MovieDataItem.HeadingItem -> binding.apply {
                    heading.visibility = View.VISIBLE
                    movieContainer.visibility = View.GONE
                    loadMoreButton.visibility = View.GONE
                    heading.text = item.title
                }
                is MovieDataItem.LoadMoreItem -> binding.apply {
                    heading.visibility = View.GONE
                    movieContainer.visibility = View.GONE
                    loadMoreButton.visibility = View.VISIBLE
                    // TODO add onClickListener
                }
            }
        }

    }

    sealed class MovieDataItem {

        data class HeadingItem(val title: String) : MovieDataItem()
        data class MovieItem(val movie: Movie) : MovieDataItem()
        data class LoadMoreItem(val page: Int) : MovieDataItem()

    }

    override fun submitList(list: MutableList<MovieDataItem>?) {
        throw IllegalStateException("Call addItems() or updateItems() instead")
    }

    override fun submitList(list: MutableList<MovieDataItem>?, commitCallback: Runnable?) {
        throw IllegalStateException("Call addItems() or updateItems() instead")
    }

    fun addItems(category: String, movies: List<Movie>) {
        movieMap.merge(category, movies) { oldMovies, newMovies ->
            oldMovies + newMovies
        }

        super.submitList(getItemList())
    }

    fun updateItems(category: String, movies: List<Movie>) {
        movieMap[category] = movies

        super.submitList(getItemList())
    }

    fun isHeading(position: Int) : Boolean = currentList[position] is MovieDataItem.HeadingItem

    fun isButton(position: Int) : Boolean = currentList[position] is MovieDataItem.LoadMoreItem

    private fun getItemList(): List<MovieDataItem> =
        movieMap.entries.fold(listOf()) { list, (category, movies) ->
            list +
                    MovieDataItem.HeadingItem(category) +
                    movies.map { MovieDataItem.MovieItem(it) } +
                    MovieDataItem.LoadMoreItem(-1)
        }
}

private class MovieDiffCallback : DiffUtil.ItemCallback<MovieDataItem>() {

    override fun areItemsTheSame(oldItem: MovieDataItem, newItem: MovieDataItem): Boolean {
        return oldItem is MovieDataItem.MovieItem
                && newItem is MovieDataItem.MovieItem
                && oldItem.movie.id == newItem.movie.id
    }

    override fun areContentsTheSame(oldItem: MovieDataItem, newItem: MovieDataItem): Boolean {
        return oldItem == newItem
    }
}