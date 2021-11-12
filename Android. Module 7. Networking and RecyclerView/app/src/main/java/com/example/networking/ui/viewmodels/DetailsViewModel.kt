package com.example.networking.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networking.model.mapper.CharacterMapper
import com.example.networking.model.dao.Character
import com.example.networking.model.dao.Episode
import com.example.networking.model.network.NetworkLayer
import com.example.networking.model.network.SharedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.w3c.dom.ls.LSException

class DetailsViewModel(
    private val sharedRepository: SharedRepository
) : ViewModel() {

    val channelCharacters = Channel<Character>()
    val channelEpisode = Channel<List<Episode>>()

    fun getCharacterById(
        id: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val character = sharedRepository.getCharacterById(id)

            channelCharacters.send(character)
        }
    }

    fun getEpisodesByIds(
        character: Character
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val episodes = sharedRepository.getCharacterById(character)

            channelEpisode.send(episodes)
        }
    }
}