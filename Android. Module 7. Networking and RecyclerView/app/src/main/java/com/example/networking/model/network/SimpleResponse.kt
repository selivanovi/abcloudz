package com.example.networking.model.network

import retrofit2.Response
import java.lang.Exception

data class SimpleResponse<T>(
    val data: Response<T>?,
    val exception: Exception?
) {

    companion object {
        fun <T> success(data: Response<T>): SimpleResponse<T> =
            SimpleResponse(
                data = data,
                exception = null
            )

        fun <T> failure(exception: Exception): SimpleResponse<T> =
            SimpleResponse(
                data = null,
                exception = exception
            )
    }

    val isSuccessful: Boolean
        get() = this.data?.isSuccessful == true

    val body: T?
        get() = data?.body()

}
