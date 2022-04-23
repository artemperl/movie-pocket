package com.ap.moviepocket.ui.discover

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.ap.model.Movie
import com.ap.moviepocket.MainCoroutineRule
import com.ap.moviepocket.data.movie.DefaultMovieRepository
import com.ap.moviepocket.domain.movie.DiscoverMoviesUseCase
import com.ap.moviepocket.domain.movie.MovieCategory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import timber.log.Timber

/**
 * Unit tests for the [DiscoverViewModel].
 */
class DiscoverViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Use test dispatcher for coroutine
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun `Call getDiscoverMoviesList with page 1 when refreshMovies is called`() = runTest {
        val mockRepo : DefaultMovieRepository = mock()
        whenever(mockRepo.getDiscoverMoviesList(1)).thenReturn(Pair(listOf(Movie("a")), 3))

        val useCaseMock = DiscoverMoviesUseCase(mockRepo, StandardTestDispatcher())
        val viewModel = DiscoverViewModel(useCaseMock)
        var result : Map<MovieCategory, Pair<List<Movie>, Int>> = mapOf()

        val job = launch(UnconfinedTestDispatcher()) {
            // collect the latest value
            viewModel.moviesMap.collect {
                result = it
            }
        }

        viewModel.refreshMovies()
        // force pending coroutine actions to be performed immediately
        runCurrent()

        verify(mockRepo).getDiscoverMoviesList(1)
        assertEquals(result.values.first().second, 3)
        assertEquals(result.values.first().first.first().id, "a")
        job.cancel()
    }

    @Test
    fun `Call getDiscoverMoviesList when loadMoreMovies is called`() = runTest {
        val mockRepo : DefaultMovieRepository = mock()
        whenever(mockRepo.getDiscoverMoviesList(1)).thenReturn(Pair(listOf(Movie("a")), 3))

        val useCaseMock = DiscoverMoviesUseCase(mockRepo, StandardTestDispatcher())
        val viewModel = DiscoverViewModel(useCaseMock)

        val job = launch(UnconfinedTestDispatcher()) {
            // collect the latest value
            viewModel.moviesMap.collect()
        }

        viewModel.loadMoreMovies(MovieCategory.DISCOVER, 3)
        // force pending coroutine actions to be performed immediately
        runCurrent()

        verify(mockRepo).getDiscoverMoviesList(3)
        job.cancel()
    }

}
