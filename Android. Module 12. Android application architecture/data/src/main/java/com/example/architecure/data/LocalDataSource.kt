package com.example.architecure.data

import com.example.architecure.data.local.entity.Movie
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getMovies(): Flow<List<Movie>>
    fun getMovieById(movieId: Int): Flow<Movie>
    suspend fun refreshMovies(movies: List<Movie>)
}