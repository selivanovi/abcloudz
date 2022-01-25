package com.example.spyfall.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val errorMutableChannel = Channel<Throwable>()
    val errorChannel = errorMutableChannel.receiveAsFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    final override val coroutineContext: CoroutineContext =
        viewModelScope.coroutineContext + coroutineExceptionHandler + Dispatchers.IO


    private fun handleError(throwable: Throwable) {
        launch {
            errorMutableChannel.send(throwable)
        }
    }
}
