package com.example.networking.network

import com.example.networking.network.characters.CharactersPageResponse
import com.example.networking.network.episodes.EpisodeResponse
import com.example.networking.network.episodes.ListOfEpisodesResponse
import retrofit2.Response

class ApiClient(
    private val rickAndMortyService: RickAndMortyService
) {

    suspend fun getCharactersPage(pageIndex: Int): SimpleResponse<CharactersPageResponse> =
        safeApiCall { rickAndMortyService.getCharactersPage(pageIndex) }

    suspend fun getEpisodesPageByIds(listOfEpisode: String): SimpleResponse<List<EpisodeResponse>> =
        safeApiCall { rickAndMortyService.getEpisodesPageByIds(listOfEpisode) }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>) =
        try {
            SimpleResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            SimpleResponse.failure(e)
        }
}
