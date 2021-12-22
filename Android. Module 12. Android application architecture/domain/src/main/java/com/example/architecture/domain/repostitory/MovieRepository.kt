package com.example.architecture.domain.repostitory

import com.example.architecture.domain.entity.MovieDomain
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getRemoteMovies() : List<MovieDomain>?
    suspend fun getRemoteMovieById(movieId: String) : MovieDomain?
    suspend fun getLocalMovies(): Flow<List<MovieDomain>>
    suspend fun getLocalMovieById(movieId: Int) : Flow<MovieDomain>
    suspend fun refreshMovies(movies: List<MovieDomain>)
}