package com.example.networking

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.networking.model.CharacterResponse
import com.example.networking.model.CharactersPageResponse
import com.example.networking.network.ApiClient
import retrofit2.Call
import retrofit2.Response

private const val CHARACTERS_STARTING_PAGE = 1

class CharactersDataSource(
    private val apiClient: ApiClient,
) : PagingSource<Int, CharacterResponse>()
{
    override fun getRefreshKey(state: PagingState<Int, CharacterResponse>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterResponse> {
        val page: Int = params.key ?: CHARACTERS_STARTING_PAGE
        val pageSize: Int = params.loadSize

        val characters = apiClient.getCharactersPage(page)


        characters.enqueue(object : retrofit2.Callback<CharactersPageResponse>{
            override fun onResponse(
                call: Call<CharactersPageResponse>,
                response: Response<CharactersPageResponse>
            ) {
                if (response.isSuccessful) {
                    val results = checkNotNull(response.body()).results
                    val nextKey = if (results.size < pageSize) null else page + 1
                    val prevKey = if (page == 0) null else page - 1

                }
            }

            override fun onFailure(call: Call<CharactersPageResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        return
    }
}