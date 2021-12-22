package com.example.architecture.domain.usecase

import com.example.architecture.domain.Constants
import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.domain.extension.LoadingError
import com.example.architecture.domain.repostitory.MovieRepository

class GetRemoteMoviesUseCase(
    private val movieRepository: MovieRepository
) {

    suspend fun execute(): List<MovieDomain>? =
        movieRepository.getRemoteMovies()

}