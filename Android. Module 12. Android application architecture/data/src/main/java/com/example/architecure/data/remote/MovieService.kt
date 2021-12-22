package com.example.architecure.data.remote

import com.example.architecure.data.Constants
import com.example.architecure.data.remote.entity.MovieResponse
import com.example.architecure.data.remote.entity.PageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("api_key") key: String,
        @Query("page") page: String
    ): Response<PageResponse>

    @GET("/movie/{movieId}"+Constants.PRIVATE_KEY)
    suspend fun getMovieById(
        @Path("movieId") movieId: String
    ): Response<MovieResponse>
}