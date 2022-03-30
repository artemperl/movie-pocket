package com.ap.moviepocket.data.movie

import com.ap.model.Movie
import com.ap.moviepocket.data.movie.tmdb.TMDBDiscoverService
import com.ap.moviepocket.data.movie.tmdb.TmdbMovieService
import com.ap.moviepocket.data.movie.tmdb.toMovie
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

interface MovieRepository {

    suspend fun getDiscoverMoviesList() : List<Movie>?

}

@Singleton
class DefaultMovieRepository @Inject constructor(
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val localMovieDataSource: LocalMovieDataSource,
    ) : MovieRepository {

    override suspend fun getDiscoverMoviesList(): List<Movie>? {
        // TODO replace hardcoded parameters with results from configuration
        return remoteMovieDataSource
            .getDiscoverMoviesList()
            ?.map { it.toMovie("https://image.tmdb.org/t/p/", "w500") }
    }

}

