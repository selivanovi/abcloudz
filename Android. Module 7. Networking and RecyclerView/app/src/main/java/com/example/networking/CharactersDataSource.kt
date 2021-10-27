package com.example.networking

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.networking.delegate.DelegateAdapterItem
import com.example.networking.mapper.CharacterMapper
import com.example.networking.model.Character
import com.example.networking.network.ApiClient


private const val CHARACTERS_STARTING_PAGE = 1

class CharactersDataSource(
    private val apiClient: ApiClient,
) : PagingSource<Int, DelegateAdapterItem>()
{
    override fun getRefreshKey(state: PagingState<Int, DelegateAdapterItem>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return  anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DelegateAdapterItem> {
        val page: Int = params.key ?: CHARACTERS_STARTING_PAGE

        val pageRequest = apiClient.getCharactersPage(page)
        pageRequest.exception?.let {
            return LoadResult.Error(it)
        }

        val characters = pageRequest.body.results.map { CharacterMapper.getFrom(it)}
        val nextPageNumber = if(pageRequest.body.info.next != null) page + 1 else null
        val prevPageNumber = if(page > 1) page - 1 else null
        return LoadResult.Page(characters, prevPageNumber, nextPageNumber)
    }
}