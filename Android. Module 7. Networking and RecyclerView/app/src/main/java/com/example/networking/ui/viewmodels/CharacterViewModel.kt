package com.example.networking.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.networking.model.network.SharedRepository
import com.example.networking.ui.delegate.DelegateAdapterItem
import com.example.networking.model.network.NetworkLayer
import kotlinx.coroutines.flow.Flow

class CharacterViewModel: ViewModel() {

     fun searchCharacter(): Flow<PagingData<DelegateAdapterItem>> =
        SharedRepository(NetworkLayer.apiService).getCharacters().cachedIn(viewModelScope)

}