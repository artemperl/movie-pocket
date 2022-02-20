package com.ap.moviepocket.data.movie.tmdb

import com.ap.moviepocket.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Service for the /movie endpoint.
 */
interface TmdbMovieService {

    @GET("/movie/{movie_id}?api_key=${BuildConfig.TMDB_API_KEY}")
    suspend fun getMovie(
        @Path("movie_id") movieId : Int
    ) : Response<TmdbMovie>

}