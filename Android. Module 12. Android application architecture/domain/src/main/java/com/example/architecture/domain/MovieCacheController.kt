package com.example.architecture.domain

import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.domain.usecase.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

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