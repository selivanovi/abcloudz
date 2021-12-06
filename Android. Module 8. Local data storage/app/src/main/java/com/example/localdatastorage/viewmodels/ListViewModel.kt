package com.example.localdatastorage.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.model.room.DonutDataBase
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

    @Suppress("UNCHECKED_CAST")
    class Factory(
        val context: Context
    ) : ViewModelProvider.Factory {

        private val dataBase =
            DonutDataBase.getInstance(context)

        private val donutsRepository =
            DonutsRepository(dataBase)

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListViewModel(donutsRepository) as T
        }
    }
}