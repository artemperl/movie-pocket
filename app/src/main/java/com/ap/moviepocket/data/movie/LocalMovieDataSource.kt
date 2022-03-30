package com.ap.moviepocket.data.movie

import com.ap.moviepocket.data.movie.tmdb.TmdbMovie
import javax.inject.Inject

class LocalMovieDataSource @Inject constructor(): MovieDataSource {
    override suspend fun getDiscoverMoviesList(): List<TmdbMovie>? {
        TODO("Not yet implemented")
    }
}