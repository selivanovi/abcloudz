package com.example.localdatastorage.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.model.room.DonutsRepository
import com.example.localdatastorage.model.room.entities.Donut
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ListViewModel(
    private val repository: DonutsRepository
) : ViewModel() {

    fun getDonuts(): Flow<DonutUI> = flow {
        mutableListOf<DonutUI>()
        viewModelScope.launch {
            repository.getDonutsAndBatters().collect {
                it
            }
            repository.getToppingsAndBatters()
        }


    }


    class Factory(private val repository: DonutsRepository) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListViewModel(repository) as T
        }
    }
}