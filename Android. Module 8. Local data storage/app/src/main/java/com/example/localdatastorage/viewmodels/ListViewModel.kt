package com.example.localdatastorage.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.model.room.DonutsRepository
import com.example.localdatastorage.utils.toDonutUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class ListViewModel(
    private val repository: DonutsRepository
) : ViewModel() {

    fun getDonutsUI(): Flow<List<DonutUI>> {
        val donutsWithBattersAndToppings = repository.getBattersAndToppingsOfDonuts()
        return donutsWithBattersAndToppings.map { list ->
            list.map { it.toDonutUI() }
        }.flowOn(Dispatchers.IO)
    }

    class Factory(private val repository: DonutsRepository) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListViewModel(repository) as T
        }
    }
}