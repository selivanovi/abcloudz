package com.example.architecure.data.remote

import android.util.Log
import com.example.architecure.data.Constants
import com.example.architecure.data.RemoteDataSource
import com.example.architecure.data.remote.entity.MovieResponse

class ApiClient(
    private val movieService: MovieService
) : RemoteDataSource {

    override suspend fun getMovies(): List<MovieResponse>? {
        val result = movieService.getMovies(Constants.PRIVATE_KEY, "1").body()?.results
        Log.d("BAG", result.toString())
        return result
    }

    override suspend fun getMovieById(movieId: String): MovieResponse? =
        movieService.getMovieById(movieId).body()
}