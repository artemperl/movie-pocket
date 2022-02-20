package com.ap.moviepocket.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ap.model.Movie
import com.ap.moviepocket.domain.UseCaseResult
import com.ap.moviepocket.domain.movie.DiscoverMoviesUseCase
import com.ap.moviepocket.domain.succeeded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val discoverMoviesUseCase: DiscoverMoviesUseCase
) : ViewModel() {

    private val _refreshing = MutableStateFlow(false)
    val refreshing = _refreshing.asStateFlow()

    val movieList = refreshing
        .filter {
            it
        }.flatMapLatest {
            discoverMoviesUseCase(Unit)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UseCaseResult.Loading)

    fun refreshMovies() {
        _refreshing.value = true
    }

}