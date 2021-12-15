package com.example.asyncoperations.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class BaseViewModel() : ViewModel(), CoroutineScope {

    val errorChannel = Channel<Throwable>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler {_, e -> handleError(e)}

    private fun handleError(e: Throwable) {
        launch {
            errorChannel.send(e)
        }
    }

    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext + coroutineExceptionHandler + Dispatchers.IO
}