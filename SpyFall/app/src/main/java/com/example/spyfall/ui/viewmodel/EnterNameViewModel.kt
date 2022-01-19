package com.example.spyfall.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.spyfall.domain.usecase.SetNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EnterNameViewModel @Inject constructor(
    private val setNameUseCase : SetNameUseCase
) : ViewModel() {

}