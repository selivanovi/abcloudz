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
import com.example.asyncoperations.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class CharacterViewModel(private val repository: CharactersRepository) : BaseViewModel() {


    val channelCharacters = repository.observeCharacter().also { it.launchIn(viewModelScope) }
        get() {
            launch {
                repository.updateCharacter()
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