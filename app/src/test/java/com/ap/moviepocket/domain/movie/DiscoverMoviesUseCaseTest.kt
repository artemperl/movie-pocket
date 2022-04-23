package com.ap.moviepocket.domain.movie

import com.ap.model.Movie
import com.ap.moviepocket.MainCoroutineRule
import com.ap.moviepocket.data.movie.DefaultMovieRepository
import com.ap.moviepocket.domain.UseCaseResult
import com.ap.moviepocket.domain.data
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DiscoverMoviesUseCaseTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Test
    fun `return movies from repository when use case is invoked`() = runTest {
        val mockRepo : DefaultMovieRepository = mock()
        whenever(mockRepo.getDiscoverMoviesList(3)).thenReturn(Pair(listOf(Movie("a")), 3))

        // when DiscoverMoviesUseCase is invoked
        val useCaseFlow = DiscoverMoviesUseCase(mockRepo, StandardTestDispatcher())(3)
        val result1 = useCaseFlow.first()

        // then a Loading result is emitted
        assertTrue(result1 is UseCaseResult.Loading)
        verify(mockRepo).getDiscoverMoviesList(3)

        val result2 = useCaseFlow.last()

        // then a success result is emitted
        assertEquals(result2.data!!.second, 3)
        verify(mockRepo, times(2)).getDiscoverMoviesList(3)
    }

}