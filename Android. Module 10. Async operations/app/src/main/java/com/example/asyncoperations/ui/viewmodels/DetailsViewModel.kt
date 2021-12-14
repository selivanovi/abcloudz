package com.example.asyncoperations.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asyncoperations.model.EpisodesRepository
import com.example.asyncoperations.model.ui.CharacterUI
import com.example.asyncoperations.model.ui.EpisodeUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val episodesRepository: EpisodesRepository
) : ViewModel() {

    val channelCharacters = Channel<List<CharacterUI>>()
    val channelEpisodes = Channel<List<EpisodeUI>>()
    val channelError = Channel<Throwable?>()

    fun getCharacterById(
        id: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
//        val result = episodesRepository.getCharactersById(id)
//
//        if (result.isSuccess) {
//            result.getOrNull()?.let { channelCharacters.send(it) }
//            Log.d("DetailsViewModel", "Success: send character")
//        } else {
//            channelError.send(result.exceptionOrNull())
//            Log.d("DetailsViewModel", "Error: send error")
//        }
    }


    fun getEpisodesByIds(
        character: CharacterUI
    ) = viewModelScope.launch(Dispatchers.IO) {
//        val result = episodesRepository.getEpisodeByIds(character)
//
//        if (result.isSuccess) {
//            result.getOrNull()?.let { channelEpisodes.send(it) }
//            Log.d("DetailsViewModel", "Success: send episodes")
//        } else {
//            channelError.send(result.exceptionOrNull())
//            Log.d("DetailsViewModel", "Error: send error")
//        }
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(private val episodesRepository: EpisodesRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailsViewModel(episodesRepository) as T
        }
    }
}