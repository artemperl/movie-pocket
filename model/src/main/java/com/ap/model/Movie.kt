package com.ap.model

import java.util.*

data class Movie(
    val id : String,
    val title : String,
    val posterUrl : String,
    val backDropUrl : String,
    val voteCount : Int,
    val voteAverage : Double,
    val releaseDate : Date,
    val genres : Set<Genre>,
    val summary : String,
)