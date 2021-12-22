package com.example.architecure.data

import com.example.architecure.data.remote.entity.MovieResponse

interface RemoteDataSource {

    suspend fun getMovies(): List<MovieResponse>?
    suspend fun getMovieById(movieId: String): MovieResponse?
}