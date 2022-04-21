package com.ap.moviepocket.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ap.model.Movie
import com.ap.moviepocket.domain.UseCaseResult
import com.ap.moviepocket.domain.movie.DiscoverMoviesUseCase
import com.ap.moviepocket.domain.movie.MovieCategory
import com.ap.moviepocket.domain.movie.MoviesRequestAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val discoverMoviesUseCase: DiscoverMoviesUseCase
) : ViewModel() {

    private val _refreshing = MutableStateFlow(false)
    val refreshing = _refreshing.asStateFlow()

    private val _moviesRequestActions = Channel<MoviesRequestAction>(capacity = Channel.CONFLATED)
    val moviesRequestActions = _moviesRequestActions.receiveAsFlow()

    private val _moviesMap : Flow<Map<MovieCategory, Pair<List<Movie>, Int>>> = moviesRequestActions
        .flatMapLatest { action ->
            // use of flatMapLatest should avoid race conditions
            var movies = moviesMap.value.toMutableMap()
            var category = MovieCategory.DISCOVER

            when (action) {
                is MoviesRequestAction.LoadMoreMovies -> {
                    category = action.category
                    when (action.category) {
                        MovieCategory.DISCOVER ->
                            discoverMoviesUseCase(action.page)
                        MovieCategory.COMEDY -> TODO()
                    }
                }
                is MoviesRequestAction.RefreshMovies -> {
                    movies = mutableMapOf()
                    discoverMoviesUseCase(1)
                }
            }.mapNotNull { result ->
                when (result) {
                    is UseCaseResult.Loading -> {
                        _refreshing.value = true
                        null
                    }
                    is UseCaseResult.Success -> {
                        _refreshing.value = false
                        val newList = movies[category]?.first?.toMutableList() ?: mutableListOf()
                        movies[category] = Pair(newList.apply { addAll(result.data.first) }, result.data.second)
                        movies
                    }
                    is UseCaseResult.Error -> {
                        // TODO error handling for UI
                        _refreshing.value = false
                        null
                    }
                }
            }
        }
    val moviesMap : StateFlow<Map<MovieCategory, Pair<List<Movie>, Int>>> = _moviesMap
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), mapOf())

    fun refreshMovies() {
        _refreshing.value = true
        _moviesRequestActions.trySend(MoviesRequestAction.RefreshMovies)
    }

    fun loadMoreMovies(category: MovieCategory, page: Int) {
        when (category) {
            MovieCategory.DISCOVER ->
                _moviesRequestActions.trySend(MoviesRequestAction.LoadMoreMovies(category, page))
            else -> TODO()
        }
    }

}