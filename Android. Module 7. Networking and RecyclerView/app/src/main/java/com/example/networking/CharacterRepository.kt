package com.example.networking

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.networking.delegate.DelegateAdapterItem
import com.example.networking.model.Character
import com.example.networking.network.ApiClient
import kotlinx.coroutines.flow.Flow

class CharacterRepository(private val apiClient: ApiClient){

    fun getCharacters(): Flow<PagingData<DelegateAdapterItem>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { CharactersDataSource(apiClient) }
        ).flow

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}