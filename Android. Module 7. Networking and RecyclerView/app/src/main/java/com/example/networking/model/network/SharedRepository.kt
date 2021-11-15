package com.example.networking.model.network

import android.content.res.Resources
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.networking.ui.delegate.DelegateAdapterItem
import com.example.networking.model.dao.Character
import com.example.networking.model.dao.Episode
import com.example.networking.model.pagin.CharactersDataSource
import com.example.networking.utils.toDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
    ): Character? {
        val request = apiClient.getCharacterById(characterOfId)

        return request.data?.body()?.toDTO()
    }


    suspend fun getEpisodeByIds(
        characters: Character
    ): List<Episode>? {
        val episodes = characters.episode?.map {
            it.substring(startIndex = it.lastIndexOf('/') + 1)
        }.toString()

        val request = apiClient.getEpisodesPageByIds(episodes)

        return request.data?.body()?.map { it.toDTO() }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}