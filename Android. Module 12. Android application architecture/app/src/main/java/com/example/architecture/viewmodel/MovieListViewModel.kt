package com.example.architecture.viewmodel

import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.domain.usecase.GetMoviesUseCase
import com.example.architecture.domain.usecase.RefreshMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieListViewModel @Inject constructor(
    private val moviesUseCase: GetMoviesUseCase,
    private val refreshLocalUseCase: RefreshMoviesUseCase
) : BaseViewModel() {


    fun observeMovie(): Flow<List<MovieDomain>> = moviesUseCase.execute()

    fun updateMovie() =
        launch {
            refreshLocalUseCase.execute()
        }
}