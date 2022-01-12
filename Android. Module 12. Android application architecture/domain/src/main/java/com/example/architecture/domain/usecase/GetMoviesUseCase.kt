package com.example.architecture.domain.usecase

import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.domain.repostitory.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

     fun execute(): Flow<List<MovieDomain>> =
        movieRepository.observeMovies()
}