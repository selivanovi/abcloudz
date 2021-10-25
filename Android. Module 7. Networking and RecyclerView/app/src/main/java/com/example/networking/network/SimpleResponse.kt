package com.example.networking.network

import retrofit2.Response
import java.lang.Exception

data class SimpleResponse<T>(
    val status: Status,
    val data: Response<T>?,
    val exception: Exception?
) {
    sealed class Status {
        object Success : Status()
        object Failure : Status()
    }

    companion object {
        fun <T> success(data: Response<T>): SimpleResponse<T> =
            SimpleResponse(
                status = Status.Success,
                data = data,
                exception = null
            )

        fun <T> failure(exception: Exception): SimpleResponse<T> =
            SimpleResponse(
                status = Status.Success,
                data = null,
                exception = exception
            )
    }

    val failed: Boolean
        get() = this.status == Status.Failure

    val isSuccessful: Boolean
        get() = !failed && this.data?.isSuccessful == true

    val body: T
        get() = this.data!!.body()!!

    val bodyNullable: T?
        get() = this.data?.body()
}
