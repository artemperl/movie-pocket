package com.ap.moviepocket.data.movie

import retrofit2.Response

/**
 * Service for the /discover/(movie|tv) endpoint. The queries for these paths can have
 * over 30 parameters, therefore each method in this service uses a specific subset of
 * these parameters.
 */
interface DiscoverService {

    suspend fun discover() : Response<>

}