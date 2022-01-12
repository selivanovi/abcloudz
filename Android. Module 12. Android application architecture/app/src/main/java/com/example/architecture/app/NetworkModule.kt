package com.example.architecture.app

import com.example.architecure.data.Constants
import com.example.architecure.data.remote.ApiClient
import com.example.architecure.data.remote.MovieService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
class NetworkModule {

    @Provides
    fun provideApiClient(
        movieService: MovieService
    ): ApiClient {
        return ApiClient(movieService)
    }

    @Provides
    fun provideMovieService(): MovieService {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create()
    }
}