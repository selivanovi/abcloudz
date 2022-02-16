package com.example.spyfall.ui.viewmodel

import android.util.Log
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
    private val logInDirections: LogInDirections
) : BaseViewModel() {

    private val successAuthorizationMutableChannel = Channel<Unit>()
    val successAuthorizationChannel = successAuthorizationMutableChannel.receiveAsFlow()

    fun logIn(name: String) {
        val userDomain = UserDomain(name = name)
        launch {
            userRepository.addUser(userDomain).collect {
                when {
                    it.isSuccess -> {
                        successAuthorizationMutableChannel.send(Unit)
                    }
                    it.isFailure -> {
                        it.exceptionOrNull()?.let { throwable ->
                            throw throwable
                        }
                    }
                }
            }
        }
    }

    fun getUser(): UserDomain? {
        Log.d("SetNameViewModel", userRepository.getUser().toString())
        return userRepository.getUser()
    }

    fun navigateToStartFragment() {
        navigateTo(logInDirections.toStart())
    }
}