package com.example.architecure.data

import com.example.architecure.data.remote.entity.MovieResponse
import com.example.architecure.data.remote.Resource

interface RemoteDataSource {

    suspend fun getMovies(): Resource<List<MovieResponse>>
    suspend fun getMovieById(movieId: String): Resource<MovieResponse>
}