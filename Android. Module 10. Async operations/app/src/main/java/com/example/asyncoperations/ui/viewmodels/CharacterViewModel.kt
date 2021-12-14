package com.example.asyncoperations.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asyncoperations.model.CharactersRepository
import com.example.asyncoperations.model.CharactersRepositoryImpl
import com.example.asyncoperations.model.network.NetworkLayer
import com.example.asyncoperations.model.room.CharacterDataBase
import com.example.asyncoperations.model.room.RoomSource
import com.example.asyncoperations.model.ui.CharacterUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CharacterViewModel(private val repository: CharactersRepository): ViewModel() {

    private val _channelError = Channel<Throwable>()
    val channelError = _channelError.receiveAsFlow()

    val channelCharacters = repository.observeCharacter().also { it.launchIn(viewModelScope) }
        get() {
            viewModelScope.launch(Dispatchers.IO) {
                val exception = repository.updateCharacter()
                exception?.let {
                    _channelError.send(it)
                }
            }
            return field
        }


    @Suppress("UNCHECKED_CAST")
    class Factory(private val context: Context) : ViewModelProvider.Factory {

        private val dataBase = CharacterDataBase.getInstance(context)

        private val localSource = RoomSource(dataBase.characterDao)
        private val remoteSource = NetworkLayer.apiService

        private val repository = CharactersRepositoryImpl(remoteSource, localSource)

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CharacterViewModel(repository) as T
        }
    }
}