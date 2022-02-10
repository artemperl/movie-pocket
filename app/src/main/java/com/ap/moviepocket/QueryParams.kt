package com.ap.moviepocket

interface QueryParams {
    fun toMap() : Map<String, String>
}