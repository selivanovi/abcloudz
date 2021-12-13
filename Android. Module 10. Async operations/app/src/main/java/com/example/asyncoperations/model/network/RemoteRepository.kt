package com.example.networking.model.network

import com.example.asyncoperations.model.ui.CharacterUI
import com.example.asyncoperations.model.ui.EpisodeUI
import com.example.asyncoperations.utils.toUI

class RemoteRepository(
    private val apiClient: ApiClient,
) {

    fun getCharacters() {

    }


    suspend fun getCharacterById(
        characterOfId: Int
    ): Result<CharacterUI?> {
        val request = apiClient.getCharacterById(characterOfId)

        request.exception?.let {
            return Result.failure(it)
        }

        return Result.success(request.body?.toUI())
    }


    suspend fun getEpisodeByIds(
        character: CharacterUI
    ): Result<List<EpisodeUI>?> {
        val episodes = character.episode?.map {
            it.substring(startIndex = it.lastIndexOf('/') + 1)
        }.toString()

        val request = apiClient.getEpisodesPageByIds(episodes)

        request.exception?.let {
            return Result.failure(it)
        }

        return Result.success(request.body?.map { it.toUI() })
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}