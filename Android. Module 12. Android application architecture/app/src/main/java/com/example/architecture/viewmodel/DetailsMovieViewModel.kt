package com.example.architecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.architecture.domain.usecase.GetMovieByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

class DetailsMovieViewModel @Inject constructor(
    private val localMovieByIdUseCase: GetMovieByIdUseCase
) : BaseViewModel() {

    fun observeMovieById(movieId: Long) =
        localMovieByIdUseCase.execute(movieId).apply { launchIn(viewModelScope) }

}