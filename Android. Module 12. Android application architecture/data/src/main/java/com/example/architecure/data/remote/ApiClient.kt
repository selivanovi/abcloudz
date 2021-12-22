package com.example.architecure.data.remote

import com.example.architecure.data.RemoteDataSource
import com.example.architecure.data.remote.entity.MovieResponse
import retrofit2.Response

class ApiClient(
    private val movieService: MovieService
) : RemoteDataSource {

    override suspend fun getMovies(): List<MovieResponse>? =
        movieService.getMovies().body()

    override suspend fun getMovieById(movieId: String): MovieResponse? =
        movieService.getMovieById(movieId).body()
}