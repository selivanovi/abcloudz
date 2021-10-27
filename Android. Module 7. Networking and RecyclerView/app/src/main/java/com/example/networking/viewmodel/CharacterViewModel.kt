package com.example.networking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.networking.network.SharedRepository
import com.example.networking.delegate.DelegateAdapterItem
import com.example.networking.network.NetworkLayer
import kotlinx.coroutines.flow.Flow

class CharacterViewModel: ViewModel() {

     fun searchCharacter(): Flow<PagingData<DelegateAdapterItem>> =
        SharedRepository(NetworkLayer.apiService).getCharacters().cachedIn(viewModelScope)

}