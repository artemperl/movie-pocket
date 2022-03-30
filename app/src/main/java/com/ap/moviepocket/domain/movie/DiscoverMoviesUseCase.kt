package com.ap.moviepocket.domain.movie

import com.ap.model.Movie
import com.ap.moviepocket.data.movie.DefaultMovieRepository
import com.ap.moviepocket.di.IoDispatcher
import com.ap.moviepocket.domain.FlowUseCase
import com.ap.moviepocket.domain.UseCaseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class DiscoverMoviesUseCase @Inject constructor(
    private val movieRepository: DefaultMovieRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<Movie>>(ioDispatcher) {

    override fun execute(parameters : Unit): Flow<UseCaseResult<List<Movie>>> {
        return flow {
            val movies = movieRepository.getDiscoverMoviesList()
            if (movies != null) {
                emit(UseCaseResult.Success(movies))
            } else {
                emit(UseCaseResult.Error(MovieNotFoundException("Discovering movies unsuccessful")))
            }
        }
    }

}

