package com.example.architecure.data.remote

import com.example.architecure.data.RemoteDataSource
import com.example.architecure.data.remote.entity.MovieResponse
import retrofit2.Response

class ApiClient (
    val movieService: MovieService
) : RemoteDataSource {

    override suspend fun getMovies(): Resource<List<MovieResponse>> =
        safeApiCall { movieService.getMovies() }

    override suspend fun getMovieById(movieId: String): Resource<MovieResponse> =
        safeApiCall { movieService.getMovieById(movieId) }

    private inline fun <T> safeApiCall(function: () -> Response<T>) =
       try {
           val response = function()
           Resource.Success(response.body())
       } catch (e: Exception) {
           Resource.Error(e)
       }

}