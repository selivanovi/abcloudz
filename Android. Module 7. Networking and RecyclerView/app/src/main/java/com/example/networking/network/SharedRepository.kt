package com.example.networking.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.networking.delegate.DelegateAdapterItem
import com.example.networking.model.Character
import com.example.networking.network.characters.CharacterResponse
import com.example.networking.network.episodes.EpisodeResponse
import com.example.networking.pagin.CharactersDataSource
import kotlinx.coroutines.flow.Flow

class SharedRepository(
    private val apiClient: ApiClient,
) {

    fun getCharacters(): Flow<PagingData<DelegateAdapterItem>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { CharactersDataSource(apiClient) }
        ).flow

    suspend fun getCharacterById(
        characterOfId: Int
    ) : CharacterResponse {
        val request = apiClient.getCharacterById(characterOfId)

        if(!request.isSuccessful){
            return CharacterResponse()
        }

        return request.body
    }


    suspend fun getEpisodeByIds(
        characters: Character
    ): List<EpisodeResponse> {
        val episodes = characters.episode.map {
            it.substring(startIndex = it.lastIndexOf('/') + 1)
        }.toString()

        val request = apiClient.getEpisodesPageByIds(episodes)

        if (!request.isSuccessful){
            return emptyList()
        }

        return request.body
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}