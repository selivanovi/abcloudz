package com.example.networking.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.networking.model.dao.Character
import com.example.networking.model.dao.Episode
import com.example.networking.model.network.SharedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val sharedRepository: SharedRepository
) : ViewModel() {

    private val channelCharacters = Channel<Character?>()
    private val channelEpisode = Channel<List<Episode>?>()

    fun getCharacterById(
        id: Int
    ): Channel<Character?> {
        viewModelScope.launch(Dispatchers.IO) {
            val character = sharedRepository.getCharacterById(id)

            channelCharacters.send(character)
        }
        return channelCharacters
    }

    fun getEpisodesByIds(
        character: Character
    ): Channel<List<Episode>?> {
        viewModelScope.launch(Dispatchers.IO) {
            val episodes = sharedRepository.getEpisodeByIds(character)

            channelEpisode.send(episodes)
        }
        return channelEpisode
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val sharedRepository: SharedRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailsViewModel(sharedRepository) as T
        }
    }
}