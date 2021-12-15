package com.example.asyncoperations.model

import android.util.Log
import com.example.asyncoperations.model.network.SimpleResponse
import com.example.asyncoperations.model.network.episodes.EpisodeResponse
import com.example.asyncoperations.model.ui.EpisodeUI
import com.example.asyncoperations.utils.ConnectingException
import com.example.asyncoperations.utils.toDTO
import com.example.asyncoperations.utils.toUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class EpisodesRepositoryImpl(
    private val remoteSource: RemoteDataSource,
    private val localSource: LocalDataSource
) : EpisodesRepository {

    private val job = Job()

    override fun observeEpisodes(): Flow<List<EpisodeUI>> =
        localSource.getEpisodes().map { list -> list.map { it.toUI() } }

    override suspend fun updateEpisodes() {

        CoroutineScope(coroutineContext + job).launch{
            launch {
                val request = remoteSource.getEpisodesPageByIds(FIRST_EPISODES_IDS)
                request.exception?.let {
                    throw ConnectingException()
                }
                request.body?.let {
                    insertEpisodes(it)
                }
                Log.d("CharactersRepository", "First")

            }
            launch{
                val request = remoteSource.getEpisodesPageByIds(SECOND_EPISODES_IDS)
                request.exception?.let {
                    throw ConnectingException()
                }
                request.body?.let {
                    insertEpisodes(it)
                }
                Log.d("CharactersRepository", "Second")

            }
            launch {
                val request = remoteSource.getEpisodesPageByIds(THIRD_EPISODES_IDS)
                request.exception?.let {
                    throw ConnectingException()
                }
                request.body?.let {
                    insertEpisodes(it)
                }
                Log.d("CharactersRepository", "Third")

            }
        }
    }


    private suspend fun insertEpisodes(episodes: List<EpisodeResponse>){
            localSource.updateEpisodes(episodes.map { it.toDTO() })
            job.cancelChildren()
    }

    companion object {
        private val FIRST_EPISODES_IDS = Array(10) { i -> i }.contentToString()
        private val SECOND_EPISODES_IDS = Array(10) { i -> i + 10 }.contentToString()
        private val THIRD_EPISODES_IDS = Array(10) { i -> i  + 20}.contentToString()
    }
}