package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WaitingGameViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val currentUser = userRepository.getUser()!!
}
