package com.ap.moviepocket.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ap.model.Movie
import com.ap.moviepocket.databinding.ListItemDiscoverMovieBinding
import com.ap.moviepocket.domain.movie.MovieCategory
import com.ap.moviepocket.ui.discover.MovieListAdapter.MovieDataItem
import com.squareup.picasso.Picasso
import java.util.*

class MovieListAdapter :
    ListAdapter<MovieDataItem, MovieListAdapter.MovieViewHolder>(MovieDiffCallback()) {

    private var dataItemMap = LinkedHashMap<MovieCategory, List<MovieDataItem>>()

    var onLoadMoreClickListener : (MovieCategory, Int) -> Unit = { _, _ -> }

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
                    heading.text = binding.root.context.getString(item.category.labelRes)
                }
                is MovieDataItem.LoadMoreItems -> binding.apply {
                    heading.visibility = View.GONE
                    movieContainer.visibility = View.GONE
                    loadMoreButton.visibility = View.VISIBLE
                    loadMoreButton.setOnClickListener {
                        onLoadMoreClickListener(item.category, item.nextPage)
                    }
                }
            }
        }

    }

    sealed class MovieDataItem {

        data class HeadingItem(val category: MovieCategory) : MovieDataItem()
        data class MovieItem(val movie: Movie) : MovieDataItem()
        data class LoadMoreItems(val category: MovieCategory, val nextPage: Int) : MovieDataItem()

    }

    override fun submitList(list: MutableList<MovieDataItem>?) {
        throw IllegalStateException("Call addItems() or updateItems() instead")
    }

    override fun submitList(list: MutableList<MovieDataItem>?, commitCallback: Runnable?) {
        throw IllegalStateException("Call addItems() or updateItems() instead")
    }

    fun updateItems(movies : Map<MovieCategory, Pair<List<Movie>, Int>>) {
        dataItemMap = LinkedHashMap<MovieCategory, List<MovieDataItem>>()
        movies.forEach { category, (movies, nextPage) ->
            dataItemMap[category] = createCategoryItems(category, movies, nextPage)
        }

        val items = getItemList()
        super.submitList(items)
    }

    private fun createCategoryItems(category: MovieCategory, movies: List<Movie>, nextPage: Int): List<MovieDataItem> {
         val itemList = listOf<MovieDataItem>() +
                 MovieDataItem.HeadingItem(category) +
                 movies.map { MovieDataItem.MovieItem(it) }

        return if (nextPage != -1) {
            itemList + MovieDataItem.LoadMoreItems(category, nextPage)
        } else {
            itemList
        }
    }

    fun isHeading(position: Int) : Boolean = currentList[position] is MovieDataItem.HeadingItem

    fun isButton(position: Int) : Boolean = currentList[position] is MovieDataItem.LoadMoreItems

    private fun getItemList(): List<MovieDataItem> =
        dataItemMap.values.fold(LinkedList<MovieDataItem>()) { resultList, categoryList ->
            resultList.addAll(categoryList)
            resultList
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


/**
 * A SpanSizeLookup to make buttons and headings full width in a grid.
 */
class SpanSizeLookup(private val adapter : MovieListAdapter, private val columnCount : Int) : GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int) =
        if (adapter.isHeading(position) || adapter.isButton(position)) {
            columnCount
        } else {
            1
        }
}