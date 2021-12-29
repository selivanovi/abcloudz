package com.example.architecture.domain.usecase

import com.example.architecture.domain.repostitory.MovieRepository
import javax.inject.Inject

class RefreshMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend fun execute() =
        movieRepository.refreshMovies()
}