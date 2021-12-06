package com.example.networking.model.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.networking.ui.delegate.DelegateAdapterItem
import com.example.networking.model.dao.Character
import com.example.networking.model.dao.Episode
import com.example.networking.model.pagin.CharactersDataSource
import com.example.networking.utils.toDTO
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
    ): Result<Character?> {
        val request = apiClient.getCharacterById(characterOfId)

        request.exception?.let {
            return Result.failure(it)
        }

        return Result.success(request.body?.toDTO())
    }


    suspend fun getEpisodeByIds(
        characters: Character
    ): Result<List<Episode>?> {
        val episodes = characters.episode?.map {
            it.substring(startIndex = it.lastIndexOf('/') + 1)
        }.toString()

        val request = apiClient.getEpisodesPageByIds(episodes)

        request.exception?.let {
            return Result.failure(it)
        }

        return Result.success(request.body?.map { it.toDTO() })
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}