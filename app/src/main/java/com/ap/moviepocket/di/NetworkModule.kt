package com.ap.moviepocket.di

import com.ap.moviepocket.data.movie.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

const val BASE_URL = "https://api.themoviedb.org/3/"

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
    fun provideMovieService(retrofit: Retrofit) {
        retrofit.create(MovieService::class.java)
    }

}