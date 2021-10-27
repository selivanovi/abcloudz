package com.example.networking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networking.mapper.EpisodeMapper
import com.example.networking.model.Episode
import com.example.networking.network.NetworkLayer
import com.example.networking.network.SharedRepository
import com.example.networking.network.characters.CharacterResponse
import com.example.networking.network.episodes.EpisodeResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {


    fun getEpisodeById(
        characterResponse: CharacterResponse
    ): List<Episode> {

        var episodes: List<Episode>? = null

        viewModelScope.launch(Dispatchers.IO) {

            val request: Deferred<List<EpisodeResponse>> =
                async { SharedRepository(NetworkLayer.apiService).getEpisodeByIds(characterResponse) }

            episodes = request.await().map { EpisodeMapper.getFrom(it) }
        }

        return episodes ?: emptyList()
    }
}