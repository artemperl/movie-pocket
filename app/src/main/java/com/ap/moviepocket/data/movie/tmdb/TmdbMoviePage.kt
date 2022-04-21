package com.ap.moviepocket.data.movie.tmdb

import com.google.gson.annotations.SerializedName

data class TmdbMoviePage(
    @SerializedName("page")
    val currentPage : Int,
    @SerializedName("total_pages")
    val totalPages : Int,
    @SerializedName("total_results")
    val totalResults : Int,
    val results : List<TmdbMovie>
)
