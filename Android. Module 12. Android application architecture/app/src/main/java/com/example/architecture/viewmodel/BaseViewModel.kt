package com.example.architecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(), CoroutineScope {

    val channelError = Channel<Throwable>()

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable -> handleException(throwable) }

    override val coroutineContext: CoroutineContext =
        viewModelScope.coroutineContext + exceptionHandler + Dispatchers.IO

    private fun handleException(throwable: Throwable) {
        launch {
            channelError.send(throwable)
        }
    }
}