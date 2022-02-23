package com.example.spyfall.ui.viewmodel

import com.example.spyfall.domain.entity.UserDomain
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.ui.base.BaseViewModel
import com.example.spyfall.ui.navigation.LogInDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val successAuthorizationMutableChannel = Channel<UserDomain>()
    val successAuthorizationChannel = successAuthorizationMutableChannel.receiveAsFlow()

    fun logIn(name: String) {
        launch {
            val user = userRepository.createUser(name)
            successAuthorizationMutableChannel.send(user)
        }
    }

    fun getUser(): UserDomain? =
        userRepository.getUser()

    fun navigateToStartFragment() =
        navigateTo(LogInDirections.toStart())
}
