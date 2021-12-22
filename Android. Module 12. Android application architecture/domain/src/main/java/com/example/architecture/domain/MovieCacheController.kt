package com.example.architecture.domain


import com.example.architecture.domain.usecase.*

class MovieCacheController(
    private val remoteMoviesUseCase: GetRemoteMoviesUseCase,
    private val refreshLocalUseCase: RefreshLocalUseCase,
) {

    suspend fun refreshMovies() {
        val remoteMovies = remoteMoviesUseCase.execute()
        remoteMovies?.let {
            refreshLocalUseCase.execute(it)
        }
    }
}