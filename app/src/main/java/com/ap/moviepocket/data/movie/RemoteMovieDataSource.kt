package com.ap.moviepocket.data.movie

import com.ap.moviepocket.data.movie.tmdb.DiscoverMoviesQueryParams
import com.ap.moviepocket.data.movie.tmdb.TMDBDiscoverService
import javax.inject.Inject
import com.ap.moviepocket.data.movie.tmdb.TmdbMovie
import com.ap.moviepocket.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class RemoteMovieDataSource @Inject constructor(
    private val tmdbDiscoverService: TMDBDiscoverService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
    ) : MovieDataSource {

    override suspend fun getDiscoverMoviesList(page : Int) : Pair<List<TmdbMovie>, Int>? {
        return withContext(ioDispatcher) {
            val queryParams = DiscoverMoviesQueryParams(page = page)
            val response = tmdbDiscoverService.discoverMovies(queryParams.toMap())
            var nextPage = -1

            return@withContext if (response.isSuccessful && (response.body() != null)) {
                if (response.body()!!.totalPages > response.body()!!.currentPage) {
                    nextPage = response.body()!!.currentPage + 1
                }
                Pair(response.body()!!.results, nextPage)
            } else {
                null
            }
        }
    }
}