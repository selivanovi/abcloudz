package com.example.architecture.domain.usecase

import com.example.architecture.domain.Constants
import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.domain.extension.LoadingError
import com.example.architecture.domain.repostitory.MovieRepository

class GetRemoteMovieByIdUseCase(
    private val movieRepository: MovieRepository
) {

    suspend fun execute(movieId: Int): MovieDomain? =
        movieRepository.getRemoteMovieById(movieId.toString())

}