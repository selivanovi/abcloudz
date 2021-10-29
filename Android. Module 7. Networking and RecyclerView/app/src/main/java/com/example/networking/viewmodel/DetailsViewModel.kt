package com.example.networking.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networking.mapper.CharacterMapper
import com.example.networking.mapper.EpisodeMapper
import com.example.networking.model.Character
import com.example.networking.model.Episode
import com.example.networking.network.NetworkLayer
import com.example.networking.network.SharedRepository
import com.example.networking.network.characters.CharacterResponse
import com.example.networking.network.episodes.EpisodeResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DetailsViewModel : ViewModel() {

    fun getCharacterById(
        id: Int
    ): Flow<Character> = flow {


        val request = SharedRepository(NetworkLayer.apiService).getCharacterById(id)

        val character = CharacterMapper.getFrom(request)

        emit(character)
    }

    fun getEpisodesByIds(
        character: Character
    ): Flow<List<Episode>> = flow {
        val request = SharedRepository(NetworkLayer.apiService).getEpisodeByIds(character)
        val episodes = request.map{
            EpisodeMapper.getFrom(it)
        }
        emit(episodes)
    }
}