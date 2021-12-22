package com.example.architecture

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.retry

fun <T> Flow<T>.retryIn(coroutineScope: CoroutineScope): Job =
    this.retry { error ->
        val handler = coroutineScope.coroutineContext[CoroutineExceptionHandler.Key]
        handler?.handleException(coroutineScope.coroutineContext, error)
        true
    }.launchIn(coroutineScope)
