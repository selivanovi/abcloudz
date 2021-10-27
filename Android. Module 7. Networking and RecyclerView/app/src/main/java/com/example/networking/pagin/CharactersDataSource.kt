package com.example.networking.pagin

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.networking.CHARACTERS_STARTING_PAGE
import com.example.networking.StatusOfCharacters
import com.example.networking.delegate.DelegateAdapterItem
import com.example.networking.mapper.AliveCharacterMapper
import com.example.networking.mapper.DeadCharacterMapper
import com.example.networking.mapper.UnknownCharacterMapper
import com.example.networking.network.ApiClient
import com.example.networking.network.characters.CharacterResponse



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

        val characters = pageRequest.body.results.map {createCharacter(it)}
        val nextPageNumber = if(pageRequest.body.info.next != null) page + 1 else null
        val prevPageNumber = if(page > 1) page - 1 else null
        return LoadResult.Page(characters, prevPageNumber, nextPageNumber)
    }

    private fun createCharacter(response: CharacterResponse) =
        when (response.status.uppercase()) {
            StatusOfCharacters.ALIVE.status.uppercase()  -> AliveCharacterMapper.getFrom(response)
            StatusOfCharacters.DEAD.status.uppercase()  -> DeadCharacterMapper.getFrom(response)
            else -> UnknownCharacterMapper.getFrom(response)
        }
}