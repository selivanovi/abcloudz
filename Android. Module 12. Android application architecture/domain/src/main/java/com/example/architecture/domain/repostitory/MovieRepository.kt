package com.example.architecture.domain.repostitory

import com.example.architecture.domain.entity.MovieDomain
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getRemoteMovies() : List<MovieDomain>?
    suspend fun getRemoteMovieById(movieId: String) : MovieDomain?
    fun getLocalMovies(): Flow<List<MovieDomain>>
    fun getLocalMovieById(movieId: Long) : Flow<MovieDomain>
    suspend fun refreshMovies(movies: List<MovieDomain>)
}