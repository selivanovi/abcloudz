package com.example.architecture.domain.usecase

import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.domain.repostitory.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetLocalMovieByIdUseCase(
    private val movieRepository: MovieRepository
) {

    fun execute(movieId: Long): Flow<MovieDomain> {
        return movieRepository.getLocalMovieById(movieId)
    }
}