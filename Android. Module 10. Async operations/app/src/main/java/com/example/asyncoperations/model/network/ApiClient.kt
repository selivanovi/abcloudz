package com.example.asyncoperations.model.network

import com.example.asyncoperations.model.RemoteDataSource
import com.example.asyncoperations.model.network.characters.CharacterResponse
import com.example.asyncoperations.model.network.episodes.EpisodeResponse
import retrofit2.Response

class ApiClient(
    private val rickAndMortyService: RickAndMortyService
) : RemoteDataSource {

    override suspend fun getCharactersByIds(listOfCharacters: String): SimpleResponse<List<CharacterResponse>> =
        safeApiCall { rickAndMortyService.getCharactersByIds(listOfCharacters) }

    override suspend fun getEpisodesPageByIds(listOfEpisode: String): SimpleResponse<List<EpisodeResponse>> =
        safeApiCall { rickAndMortyService.getEpisodesByIds(listOfEpisode) }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>) =
        try {
            SimpleResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            SimpleResponse.failure(e)
        }
}
