package com.example.networking.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.networking.model.mapper.CharacterMapper
import com.example.networking.model.mapper.EpisodeMapper
import com.example.networking.model.dao.Character
import com.example.networking.model.dao.Episode
import com.example.networking.model.network.NetworkLayer
import com.example.networking.model.network.SharedRepository
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