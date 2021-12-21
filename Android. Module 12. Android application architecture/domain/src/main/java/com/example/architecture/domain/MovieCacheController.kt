package com.example.architecture.domain

import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.domain.usecase.*
import kotlinx.coroutines.flow.Flow

class MovieCacheController(
    private val localMoviesUseCase: GetLocalMoviesUseCase,
    private val remoteMoviesUseCase: GetRemoteMoviesUseCase,
    private val localMovieByIdUseCase: GetLocalMovieByIdUseCase,
    private val remoteMovieByIdUseCase: GetRemoteMovieByIdUseCase,
    private val refreshLocalUseCase: RefreshLocalUseCase,
) {

    suspend fun loadMovies(): Flow<List<MovieDomain>>{
        val remoteMovies = remoteMoviesUseCase.execute()
        remoteMovies?.let {
            refreshLocalUseCase.execute(it)
        }

        return localMoviesUseCase.execute()
    }

    suspend fun loadMoviesById(movieId: Int): Flow<MovieDomain>{
        val remoteMovie = remoteMovieByIdUseCase.execute(movieId)
        remoteMovie?.let {
            refreshLocalUseCase.execute(listOf(it))
        }

        return localMovieByIdUseCase.execute(movieId)
    }
}