package com.example.networking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.networking.CharacterRepository
import com.example.networking.model.Character
import com.example.networking.network.NetworkLayer
import kotlinx.coroutines.flow.Flow

class CharacterViewModel: ViewModel() {

     fun searchCharacter(): Flow<PagingData<Character>> =
        CharacterRepository(NetworkLayer.apiService).getCharacters().cachedIn(viewModelScope)



}