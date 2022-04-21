package com.ap.moviepocket.domain.movie

import com.ap.model.Movie
import com.ap.moviepocket.data.movie.DefaultMovieRepository
import com.ap.moviepocket.di.IoDispatcher
import com.ap.moviepocket.domain.FlowUseCase
import com.ap.moviepocket.domain.UseCaseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DiscoverMoviesUseCase @Inject constructor(
    private val movieRepository: DefaultMovieRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Int, Pair<List<Movie>, Int>>(ioDispatcher) {

    /**
     * Returns a flow that emits the list of requested movies and the next page number as a pair
     */
    override fun execute(parameters : Int): Flow<UseCaseResult<Pair<List<Movie>, Int>>> {
        return flow {
            val movies = movieRepository.getDiscoverMoviesList(parameters)
            if (movies != null) {
                emit(UseCaseResult.Success(movies))
            } else {
                emit(UseCaseResult.Error(MovieNotFoundException("Discovering movies unsuccessful")))
            }
        }
    }

}

