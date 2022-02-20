package com.ap.moviepocket.data.movie

import com.ap.moviepocket.data.movie.tmdb.DiscoverMoviesQueryParams
import com.ap.moviepocket.data.movie.tmdb.TMDBDiscoverService
import javax.inject.Inject
import com.ap.moviepocket.data.movie.tmdb.TmdbMovie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoteMovieDataSource @Inject constructor(
    val tmdbDiscoverService: TMDBDiscoverService,
    private val ioDispatcher: CoroutineDispatcher
    ) : MovieDataSource {

    override suspend fun getDiscoverMoviesList() : List<TmdbMovie>? {
        return withContext(ioDispatcher) {
            val response = tmdbDiscoverService.discoverMovies(DiscoverMoviesQueryParams().toMap())

            (response.takeIf { response.isSuccessful } ?: return@withContext null).body()?.results ?: listOf()
        }
    }
}