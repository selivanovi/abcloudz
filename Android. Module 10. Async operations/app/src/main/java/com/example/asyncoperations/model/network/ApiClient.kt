package com.example.networking.model.network

import com.example.networking.model.network.characters.CharacterResponse
import com.example.networking.model.network.characters.CharactersPageResponse
import com.example.networking.model.network.episodes.EpisodeResponse
import retrofit2.Response

class ApiClient(
    private val rickAndMortyService: RickAndMortyService
) {

    suspend fun getCharactersPage(pageIndex: Int): SimpleResponse<CharactersPageResponse> =
        safeApiCall { rickAndMortyService.getCharactersPage(pageIndex) }

    suspend fun getCharacterById(idOfCharacter: Int): SimpleResponse<CharacterResponse> =
        safeApiCall { rickAndMortyService.getCharacterById(idOfCharacter) }


    suspend fun getEpisodesPageByIds(listOfEpisode: String): SimpleResponse<List<EpisodeResponse>> =
        safeApiCall { rickAndMortyService.getEpisodesPageByIds(listOfEpisode) }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>) =
        try {
            SimpleResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            SimpleResponse.failure(e)
        }
}
