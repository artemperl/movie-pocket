package com.ap.moviepocket.di

import com.ap.moviepocket.BuildConfig
import com.ap.moviepocket.data.movie.tmdb.TMDBDiscoverService
import com.ap.moviepocket.data.movie.tmdb.TmdbMovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

const val BASE_URL = "https://api.themoviedb.org/3/"
const val TMDP_API_KEY = BuildConfig.TMDB_API_KEY

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun proideOkHttpClient() : OkHttpClient {
        val okHttpClient = OkHttpClient()
        return okHttpClient
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder().apply {
            client(okHttpClient)
            baseUrl(BASE_URL)
        }.build()
    }

    @Provides
    fun provideMovieService(retrofit: Retrofit) : TmdbMovieService {
        return retrofit.create(TmdbMovieService::class.java)
    }

    @Provides
    fun provideDiscoverService(retrofit: Retrofit) : TMDBDiscoverService {
        return retrofit.create(TMDBDiscoverService::class.java)
    }

}