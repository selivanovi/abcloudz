package com.example.asyncoperations.model

import com.example.asyncoperations.model.ui.CharacterUI
import com.example.asyncoperations.model.ui.EpisodeUI
import kotlinx.coroutines.flow.Flow

interface EpisodesRepository  {

    fun observeEpisodes(): Flow<List<EpisodeUI>>
    suspend fun updateEpisodes()
}