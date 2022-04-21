package com.ap.moviepocket.domain.movie

import com.ap.moviepocket.R

sealed class MoviesRequestAction {
    object RefreshMovies : MoviesRequestAction()
    class LoadMoreMovies(val category : MovieCategory, val page : Int = 1) : MoviesRequestAction()
}

enum class MovieCategory(val id : Int, val labelRes : Int) {
    DISCOVER(1, R.string.movie_category_discover),
    COMEDY(2, R.string.movie_category_comedy)
}