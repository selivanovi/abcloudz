package com.example.networking.ui.viewmodels

import android.util.Log
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
            Log.d("DetailsViewModel", "Success: send character")
        } else {
            channelError.send(result.exceptionOrNull())
            Log.d("DetailsViewModel", "Error: send error")
        }
    }


    fun getEpisodesByIds(
        character: Character
    ) = viewModelScope.launch(Dispatchers.IO) {
        val result = sharedRepository.getEpisodeByIds(character)

        if (result.isSuccess) {
            result.getOrNull()?.let { channelEpisodes.send(it) }
            Log.d("DetailsViewModel", "Success: send episodes")
        } else {
            channelError.send(result.exceptionOrNull())
            Log.d("DetailsViewModel", "Error: send error")
        }
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(private val sharedRepository: SharedRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailsViewModel(sharedRepository) as T
        }
    }
}