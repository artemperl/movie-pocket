package com.ap.moviepocket.data.movie

import com.ap.model.Movie
import com.ap.moviepocket.data.movie.tmdb.TmdbMovie

interface MovieDataSource {

    suspend fun getDiscoverMoviesList() : List<TmdbMovie>?

}