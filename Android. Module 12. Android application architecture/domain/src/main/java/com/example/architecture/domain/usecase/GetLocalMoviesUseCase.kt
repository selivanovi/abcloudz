package com.example.architecture.domain.usecase

import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.domain.repostitory.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetLocalMoviesUseCase(
    private val movieRepository: MovieRepository
) {

     suspend fun execute(): Flow<List<MovieDomain>> =
        movieRepository.getLocalMovies()

}