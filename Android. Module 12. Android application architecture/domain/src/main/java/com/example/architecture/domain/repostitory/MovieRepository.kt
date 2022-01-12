package com.example.architecture.domain.repostitory

import com.example.architecture.domain.entity.MovieDomain
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun observeMovies(): Flow<List<MovieDomain>>
    fun observeMovieById(movieId: Long): Flow<MovieDomain>
    suspend fun refreshMovies()
}