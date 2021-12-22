package com.example.architecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.architecture.domain.MovieCacheController
import com.example.architecture.domain.usecase.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val localMoviesUseCase: GetLocalMoviesUseCase,
    private val cacheController: MovieCacheController,

) : BaseViewModel() {

    val channelMovie = localMoviesUseCase.execute().also { it.launchIn(viewModelScope) }
        get() {
            launch {
                cacheController.refreshMovies()
            }
            return field
        }

    class Factory(
        private val localMoviesUseCase: GetLocalMoviesUseCase,
        private val cacheController: MovieCacheController
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MovieListViewModel(localMoviesUseCase, cacheController) as T
        }
    }
}