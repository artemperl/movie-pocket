package com.ap.moviepocket.data.movie.tmdb

import com.google.gson.annotations.SerializedName
import java.util.*

data class TmdbMovie(
    val id : String?,
    val title : String?,
    @SerializedName("poster_path")
    val posterPath : String?,
    @SerializedName("backdrop_path")
    val backdropPath : String?,
    @SerializedName("vote_count")
    val voteCount : Int?,
    @SerializedName("vote_average")
    val vote_average : Double?,
    @SerializedName("release_date")
    val releaseDate : Date?,
    val genres : Set<TmdbGenre>?,
    val overview : String?
)
