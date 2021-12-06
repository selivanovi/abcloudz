package com.example.networking.model.pagin

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.networking.Constants
import com.example.networking.StatusOfCharacters
import com.example.networking.ui.delegate.DelegateAdapterItem
import com.example.networking.model.network.ApiClient
import com.example.networking.model.network.characters.CharacterResponse
import com.example.networking.utils.toAliveCharacter
import com.example.networking.utils.toDeadCharacter
import com.example.networking.utils.toUnknownCharacter


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
        val page: Int = params.key ?: Constants.CHARACTERS_STARTING_PAGE

        val pageRequest = apiClient.getCharactersPage(page)
        pageRequest.exception?.let {
            return LoadResult.Error(it)
        }

        val characters = pageRequest.body?.results?.map { createCharacter(it) }!!
        val nextPageNumber = if(pageRequest.body?.info?.next != null) page + 1 else null
        val prevPageNumber = if(page > 1) page - 1 else null
        return LoadResult.Page(characters, prevPageNumber, nextPageNumber)
    }

    private fun createCharacter(response: CharacterResponse) =
        when (response.status?.uppercase()) {
            StatusOfCharacters.ALIVE.status.uppercase()  -> response.toAliveCharacter()
            StatusOfCharacters.DEAD.status.uppercase()  -> response.toDeadCharacter()
            else -> response.toUnknownCharacter()
        }
}