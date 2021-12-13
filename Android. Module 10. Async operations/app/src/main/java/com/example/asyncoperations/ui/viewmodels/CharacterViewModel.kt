package com.example.networking.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.networking.model.network.RemoteRepository
import com.example.networking.ui.delegate.DelegateAdapterItem
import com.example.networking.model.network.NetworkLayer
import kotlinx.coroutines.flow.Flow

class CharacterViewModel: ViewModel() {

     fun searchCharacter(): Flow<PagingData<DelegateAdapterItem>> =
        RemoteRepository(NetworkLayer.apiService).getCharacters().cachedIn(viewModelScope)

}