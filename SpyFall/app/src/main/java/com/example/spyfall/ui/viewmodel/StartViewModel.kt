package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.entity.UserDomain
import com.example.spyfall.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    fun getUser() : UserDomain? {
        return userRepository.getUser()
    }
}