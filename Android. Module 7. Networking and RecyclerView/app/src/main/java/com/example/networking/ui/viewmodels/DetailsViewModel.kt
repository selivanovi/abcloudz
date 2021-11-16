package com.example.networking.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.networking.model.dao.Character
import com.example.networking.model.dao.Episode
import com.example.networking.model.network.SharedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val sharedRepository: SharedRepository
) : ViewModel() {

    val channelCharacters = Channel<Character>()
    val channelEpisodes = Channel<List<Episode>>()
    val channelError = Channel<Throwable?>()

    fun getCharacterById(
        id: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        val result = sharedRepository.getCharacterById(id)

        if (result.isSuccess) {
            result.getOrNull()?.let { channelCharacters.send(it) }
            channelError.close()
        }
        else {
            channelError.send(result.exceptionOrNull())
            channelCharacters.close()
        }
    }


    fun getEpisodesByIds(
        character: Character
    ) = viewModelScope.launch(Dispatchers.IO) {
        val result = sharedRepository.getEpisodeByIds(character)

        if (result.isSuccess)
            result.getOrNull()?.let { channelEpisodes.send(it) }
        else channelError.send(result.exceptionOrNull())
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(private val sharedRepository: SharedRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailsViewModel(sharedRepository) as T
        }
    }
}