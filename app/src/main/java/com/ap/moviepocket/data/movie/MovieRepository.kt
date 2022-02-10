package com.ap.moviepocket.data.movie

import com.ap.moviepocket.data.movie.tmdb.TMDBDiscoverService
import com.ap.moviepocket.data.movie.tmdb.TmdbMovieService
import javax.inject.Inject
import javax.inject.Singleton

interface MovieRepository {

}

@Singleton
class DefaultMovieRepository @Inject constructor(
    private val discoverService : TMDBDiscoverService,
    private val movieService: TmdbMovieService
    ) {
}

