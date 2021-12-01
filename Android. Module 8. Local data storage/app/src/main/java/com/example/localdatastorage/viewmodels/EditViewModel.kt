package com.example.localdatastorage.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.model.room.DonutDataBase
import com.example.localdatastorage.model.room.DonutsRepository
import com.example.localdatastorage.model.room.entities.DonutBatterCrossRef
import com.example.localdatastorage.model.room.entities.DonutToppingCrossRef
import com.example.localdatastorage.utils.getDonut
import com.example.localdatastorage.utils.toDonutUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EditViewModel(private val repository: DonutsRepository) : ViewModel() {

    fun getDonutUI(idDonut: Int): Flow<DonutUI> {
        val donutWithBatterAndToppings = repository.getBattersAndToppingsOfDonut(idDonut)
        return donutWithBatterAndToppings.map { it.toDonutUI() }.flowOn(Dispatchers.IO)
    }

    fun saveDonut(donutUI: DonutUI) {
        val donut = donutUI.getDonut()
        val batterCrossRefs = donutUI.batter.map { DonutBatterCrossRef(donutUI.id, it.idBatter) }
        val toppingCrossRefs =
            donutUI.topping.map { DonutToppingCrossRef(donutUI.id, it.idTopping) }

        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAndWriteDonutWithBattersAndToppings(
                donut,
                batterCrossRefs,
                toppingCrossRefs
            )
        }
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
            return EditViewModel(donutsRepository) as T
        }
    }
}