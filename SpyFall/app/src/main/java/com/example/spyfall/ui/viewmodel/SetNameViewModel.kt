package com.example.spyfall.ui.viewmodel

import android.util.Log
import com.example.spyfall.data.repository.UserRepository
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
        launch {
            userRepository.addUserName(name).collect {
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

    fun getName(): String? {
        Log.d("SetNameViewModel", userRepository.getUserName().toString())
        return userRepository.getUserName()
    }

    fun logOut() {

        val name = getName()

        name?.let {
            launch {
                userRepository.deleteName(it)
            }
        }
    }
}