package com.example.networking.network

import com.example.networking.network.characters.CharactersPageResponse
import retrofit2.Response

class ApiClient(
    private val rickAndMortyService: RickAndMortyService
) {

    suspend fun getCharactersPage(pageIndex: Int): SimpleResponse<CharactersPageResponse> =
        safeApiCall { rickAndMortyService.getCharactersPage(pageIndex) }


    private inline fun <T> safeApiCall(apiCall: () -> Response<T>) =
        try {
            SimpleResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            SimpleResponse.failure(e)
        }
}
