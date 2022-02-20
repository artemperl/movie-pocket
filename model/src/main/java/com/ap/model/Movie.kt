package com.ap.model

import java.util.*

data class Movie(
    val id : String,
    val title : String? = null,
    val posterUrl : String? = null,
    val backDropUrl : String? = null,
    val voteCount : Int? = null,
    val voteAverage : Double? = null,
    val releaseDate : Date? = null,
    val genres : Set<Genre> = setOf(),
    val summary : String? = null
)