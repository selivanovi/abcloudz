package com.example.spyfall.ui.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.example.spyfall.ui.navigation.NavEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel() : ViewModel(), CoroutineScope {

    protected val errorMutableChannel = Channel<Throwable>()
    val errorChannel = errorMutableChannel.receiveAsFlow()

    private val navigationMutableChannel = Channel<NavEvent>()
    val navigationChannel = navigationMutableChannel.receiveAsFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    final override val coroutineContext: CoroutineContext =
        viewModelScope.coroutineContext + coroutineExceptionHandler + Dispatchers.IO

    private fun handleError(throwable: Throwable) {
        Log.e("BaseViewModel", throwable.toString())
        launch {
            errorMutableChannel.send(throwable)
        }
    }

    fun clear() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    fun navigateTo(navDirections: NavDirections) = launch {
        navigationMutableChannel.send(NavEvent.To(navDirections))
    }

    fun navigateUp() = launch {
        navigationMutableChannel.send(NavEvent.Up)
    }

    fun navigateBack() = launch {
        navigationMutableChannel.send(NavEvent.Back)
    }
}
