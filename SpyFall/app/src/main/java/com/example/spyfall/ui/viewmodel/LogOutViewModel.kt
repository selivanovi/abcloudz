package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogOutViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    fun logOut() {

        val user = userRepository.getUser()

        user?.let {
            launch {
                userRepository.deleteUser(it)
            }
        }
    }
}