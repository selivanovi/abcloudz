package com.example.localdatastorage.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.model.room.DonutsRepository
import com.example.localdatastorage.model.room.entities.Donut
import com.example.localdatastorage.model.room.entities.DonutBatterCrossRef
import com.example.localdatastorage.model.room.entities.DonutToppingCrossRef
import com.example.localdatastorage.utils.getDonut
import com.example.localdatastorage.utils.toDonutUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class EditViewModel(private val repository: DonutsRepository) : ViewModel() {

    val channelDonutUI = Channel<DonutUI>()

    /**
     * Function is getting DonutWithBattersAndToppings before sending
     * DonutUI to Channel<DonutUI>
     * */
    fun getDonutUI(idDonut: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val donutWithBatterAndToppings = repository.getBattersAndToppingsOfDonut(idDonut)
            channelDonutUI.send(donutWithBatterAndToppings.toDonutUI())
        }
    }

    /**
     * Function is getting Donut, DonutWithBatters and DonutWithToppings from DonutUI
     * and sending to DataBase data and also updating dependency between Donut - Batters,
     * Donut - Toppings
     * */
    fun saveDonut(donutUI: DonutUI) {
        Log.d("EditViewModel", "DonutUI: $donutUI")
        val donut = donutUI.getDonut()
        Log.d("EditViewModel", "Donut: $donut")
        val batterCrossRefs = donutUI.batter.map { DonutBatterCrossRef(donutUI.id, it.idBatter) }
        Log.d("EditViewModel", "Batter cross ref: $batterCrossRefs")
        val toppingCrossRefs =
            donutUI.topping.map { DonutToppingCrossRef(donutUI.id, it.idTopping) }
        Log.d("EditViewModel", "Topping cross ref: $toppingCrossRefs")

        deleteDonutBatterCrossRef(donut.idDonut)
        deleteDonutToppingCrossRef(donut.idDonut)
        insertDonut(donut)
        insertDonutBatterCrossRefs(batterCrossRefs)
        insertDonutToppingCrossRefs(toppingCrossRefs)
    }

    fun insertDonutToppingCrossRefs(toppingCrossRefs: List<DonutToppingCrossRef>) {
        Log.d("EditViewModel", "insertDonutToppingCrossRefs")
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertDonutToppingCrossRefs(toppingCrossRefs)
        }
    }

    fun insertDonutBatterCrossRefs(batterCrossRefs: List<DonutBatterCrossRef>) {
        Log.d("EditViewModel", "insertDonutBatterCrossRefs")
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertDonutBatterCrossRefs(batterCrossRefs)
        }
    }

    fun deleteDonutBatterCrossRef(idDonut: Int) {
        Log.d("EditViewModel", "deleteDonutBatterCrossRef")
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDonutBatterCrossRef(idDonut)
        }
    }

    fun deleteDonutToppingCrossRef(idDonut: Int) {
        Log.d("EditViewModel", "deleteDonutToppingCrossRef")
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDonutToppingCrossRef(idDonut)
        }
    }

    fun insertDonut(donut: Donut) {
        Log.d("EditViewModel", "insertDonut")
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertDonut(donut)
        }
    }

    class Factory(private val repository: DonutsRepository) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditViewModel(repository) as T
        }
    }
}