package com.example.spyfall.ui.viewmodel

import android.util.Log
import com.example.spyfall.domain.entity.User
import com.example.spyfall.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetNameViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val successAuthorizationMutableChannel = Channel<Unit>()
    val successAuthorizationChannel = successAuthorizationMutableChannel.receiveAsFlow()

    fun logIn(name: String) {
        val user = User(name = name)
        launch {
            userRepository.addUser(user).collect {
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

    fun getUser(): User? {
        Log.d("SetNameViewModel", userRepository.getUser().toString())
        return userRepository.getUser()
    }

    fun logOut() {

        val user = userRepository.getUser()

        user?.let {
            launch {
                userRepository.deleteUser(it)
            }
        }
    }
}