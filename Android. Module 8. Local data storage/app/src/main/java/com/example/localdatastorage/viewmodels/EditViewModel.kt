package com.example.localdatastorage.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.model.room.DonutsRepository
import com.example.localdatastorage.model.room.entities.DonutBatterCrossRef
import com.example.localdatastorage.model.room.entities.DonutToppingCrossRef
import com.example.localdatastorage.utils.getDonut
import com.example.localdatastorage.utils.toDonutUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EditViewModel(private val repository: DonutsRepository) : ViewModel() {

    private val _channelDonutUI = Channel<DonutUI>()
    val channelDonutUI = _channelDonutUI.receiveAsFlow()

    /**
     * Function is getting DonutWithBattersAndToppings before sending
     * DonutUI to Channel<DonutUI>
     * */
    fun getDonutUI(idDonut: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val donutWithBatterAndToppings = repository.getBattersAndToppingsOfDonut(idDonut)
            _channelDonutUI.send(donutWithBatterAndToppings.toDonutUI())
        }
    }

    /**
     * Function is getting Donut, DonutWithBatters and DonutWithToppings from DonutUI
     * and sending to DataBase data and also updating dependency between Donut - Batters,
     * Donut - Toppings
     * */
    fun saveDonut(donutUI: DonutUI) {
        val donut = donutUI.getDonut()
        val batterCrossRefs = donutUI.batter.map { DonutBatterCrossRef(donutUI.id, it.idBatter) }
        val toppingCrossRefs =
            donutUI.topping.map { DonutToppingCrossRef(donutUI.id, it.idTopping) }

        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteDonutBatterCrossRef(donut.idDonut)
            repository.deleteDonutToppingCrossRef(donut.idDonut)
            repository.insertDonut(donut)
            repository.insertDonutBatterCrossRefs(batterCrossRefs)
            repository.insertDonutToppingCrossRefs(toppingCrossRefs)
        }

    }

    class Factory(private val repository: DonutsRepository) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditViewModel(repository) as T
        }
    }
}