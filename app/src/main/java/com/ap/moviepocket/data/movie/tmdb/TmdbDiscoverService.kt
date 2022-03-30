package com.ap.moviepocket.data.movie.tmdb

import com.ap.moviepocket.BuildConfig
import com.ap.moviepocket.QueryParams
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Service for the /discover/(movie|tv) endpoint.
 */
interface TMDBDiscoverService {


    @GET("discover/movie?api_key=${BuildConfig.TMDB_API_KEY}")
    suspend fun discoverMovies(
        @QueryMap queryParams : Map<String, String>
    ) : Response<TmdbMoviePage>


}

class DiscoverMoviesQueryParams(
    val minVoteAverage: Int? = null,
    val minVotes: Int? = null
) : QueryParams {
    override fun toMap(): Map<String, String> = HashMap<String, String>().apply {
        minVoteAverage?.let {  put("vote_average.gte", "$minVoteAverage") }
        minVotes?.let { put("vote_count.gte", "$minVotes") }
    }
}