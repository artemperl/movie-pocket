package com.ap.moviepocket.data.movie

import com.ap.moviepocket.data.movie.tmdb.TmdbMovie
import javax.inject.Inject

class LocalMovieDataSource @Inject constructor(): MovieDataSource {
    override suspend fun getDiscoverMoviesList(page: Int): Pair<List<TmdbMovie>, Int>? {
        TODO("Not yet implemented")
    }
}