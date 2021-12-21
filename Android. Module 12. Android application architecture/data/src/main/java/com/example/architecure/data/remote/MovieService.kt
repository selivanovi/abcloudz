package com.example.architecure.data.remote

import com.example.architecure.data.Constants
import com.example.architecure.data.remote.entity.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    @GET("/movie/popular"+Constants.PRIVATE_KEY)
    suspend fun getMovies(): Response<List<MovieResponse>>

    @GET("/movie/{movieId}"+Constants.PRIVATE_KEY)
    suspend fun getMovieById(
        @Path("movieId") movieId: String
    ): Response<MovieResponse>
}