package com.ap.moviepocket.data.movie.tmdb

import com.ap.model.Genre
import com.ap.model.Movie
import com.google.gson.annotations.SerializedName
import java.lang.IllegalArgumentException
import java.util.*

data class TmdbMovie(
    val id : String,
    val title : String?,
    @SerializedName("poster_path")
    val posterPath : String?,
    @SerializedName("backdrop_path")
    val backdropPath : String?,
    @SerializedName("vote_count")
    val voteCount : Int?,
    @SerializedName("vote_average")
    val voteAverage : Double?,
    @SerializedName("release_date")
    val releaseDate : Date?,
    val genres : Set<TmdbGenre>?,
    val overview : String?
)

data class TmdbGenre(
    val id : String?,
    val name : String?
)

fun TmdbGenre.toGenre() : Genre {
    name ?: return Genre.UNKNOWN

    return try {
        Genre.valueOf(name.uppercase())
    } catch (e : IllegalArgumentException) {
        Genre.UNKNOWN
    }
}

fun Set<TmdbGenre>?.toGenres() : Set<Genre> {
    this ?: return setOf()
    return map { it.toGenre() }.toSet()
}


fun TmdbMovie.toMovie(baseUrl : String, size : String) : Movie {
    return Movie(
        id = id,
        title = title,
        posterUrl = posterPath?.let { "${baseUrl}${size}/${posterPath}" },
        backDropUrl = backdropPath?.let { "${baseUrl}${size}/${backdropPath}" },
        voteCount = voteCount,
        voteAverage = voteAverage,
        releaseDate = releaseDate,
        genres = genres.toGenres(),
        summary = overview
    )
}