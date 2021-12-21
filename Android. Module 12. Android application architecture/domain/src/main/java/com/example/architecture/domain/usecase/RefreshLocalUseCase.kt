package com.example.architecture.domain.usecase

import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.domain.repostitory.MovieRepository

class RefreshLocalUseCase(
    private val movieRepository: MovieRepository
) {

    suspend fun execute(movies: List<MovieDomain>) =
        movieRepository.refreshMovies(movies)
}