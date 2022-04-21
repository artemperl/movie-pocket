package com.ap.moviepocket.data.movie

import com.ap.model.Movie
import com.ap.moviepocket.data.movie.tmdb.TmdbMovie

interface MovieDataSource {

    suspend fun getDiscoverMoviesList(page : Int) : Pair<List<TmdbMovie>, Int>?

}