package com.example.architecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.architecture.domain.MovieCacheController
import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.domain.usecase.GetLocalMovieByIdUseCase
import com.example.architecture.domain.usecase.GetLocalMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn

class DetailsMovieViewModel(
    private val localMovieByIdUseCase: GetLocalMovieByIdUseCase
) : BaseViewModel() {

    fun loadMovieById(movieId: Long) =
        localMovieByIdUseCase.execute(movieId)

    class Factory(
        private val localMovieByIdUseCase: GetLocalMovieByIdUseCase,
        private val cacheController: MovieCacheController
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailsMovieViewModel(localMovieByIdUseCase) as T
        }
    }
}