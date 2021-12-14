package com.example.asyncoperations.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asyncoperations.model.EpisodesRepositoryImpl
import com.example.asyncoperations.model.EpisodesRepository
import com.example.asyncoperations.model.network.NetworkLayer
import com.example.asyncoperations.model.room.CharacterDataBase
import com.example.asyncoperations.model.room.RoomSource
import com.example.asyncoperations.model.ui.CharacterUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EpisodesViewModel(
    private val repository: EpisodesRepository
) : ViewModel() {

    private val _channelError = Channel<Throwable>()
    val channelError = _channelError.receiveAsFlow()

    val channelCharacters = repository.observeEpisodes().also { it.launchIn(viewModelScope) }
        get() {
            viewModelScope.launch(Dispatchers.IO) {
                val exception = repository.updateEpisodes()
                exception?.let {
                    _channelError.send(it)
                }
            }
            return field
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
    class Factory(private val context: Context) : ViewModelProvider.Factory {

        private val dataBase = CharacterDataBase.getInstance(context)

        private val localSource = RoomSource(dataBase.characterDao)
        private val remoteSource = NetworkLayer.apiService

        private val repository = EpisodesRepositoryImpl(remoteSource, localSource)

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EpisodesViewModel(repository) as T
        }
    }
}