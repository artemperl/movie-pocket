package com.ap.moviepocket.data.movie

import com.ap.model.Movie
import com.ap.moviepocket.data.movie.tmdb.TMDBDiscoverService
import com.ap.moviepocket.data.movie.tmdb.TmdbMovieService
import com.ap.moviepocket.data.movie.tmdb.toMovie
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

interface MovieRepository {

    suspend fun getDiscoverMoviesList(page: Int) : Pair<List<Movie>, Int>?

}

@Singleton
class DefaultMovieRepository @Inject constructor(
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val localMovieDataSource: LocalMovieDataSource,
    ) : MovieRepository {

    override suspend fun getDiscoverMoviesList(page : Int): Pair<List<Movie>, Int>? {
        // TODO replace hardcoded parameters with results from configuration
        val remoteResult = remoteMovieDataSource.getDiscoverMoviesList(page)
        return if (remoteResult != null) {
            Pair(
                remoteResult.first.map { it.toMovie("https://image.tmdb.org/t/p/", "w500") },
                remoteResult.second
            )
        } else {
            null
        }
    }

}

