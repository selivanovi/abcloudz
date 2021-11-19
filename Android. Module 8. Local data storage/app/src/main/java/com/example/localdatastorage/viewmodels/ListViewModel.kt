package com.example.localdatastorage.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.model.room.DonutsRepository
import com.example.localdatastorage.model.room.entities.Donut
import com.example.localdatastorage.model.room.entities.reletions.DonutWithBatters
import com.example.localdatastorage.model.room.entities.reletions.DonutWithToppings
import com.example.localdatastorage.utils.toDonutUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ListViewModel(
    private val repository: DonutsRepository
) : ViewModel() {

    fun getDonutsUI(): Flow<List<DonutUI>> {
        val donutsAndToppings = repository.getDonutsAndToppings()
        val donutsAndBatters = repository.getDonutsAndBatters()
        return donutsAndBatters.zip(donutsAndToppings) { donuts, toppings ->
            createListOfDonutUI(donuts, toppings)
        }.flowOn(Dispatchers.IO)
    }

    private fun createListOfDonutUI(
        donutsAndBatters: List<DonutWithBatters>,
        donutsAndToppings: List<DonutWithToppings>
    ): List<DonutUI> {
        val mutableList = mutableListOf<DonutUI>()
        donutsAndBatters.forEachIndexed { index, donutWithBatters ->
            val donutUI = donutWithBatters.donut.toDonutUI()
            donutUI.batter =
                donutWithBatters.batter.map { it.type }
                    .toString()
                    .removePrefix("[")
                    .removeSuffix("]")
                    .replace(",", "\n\t")
            donutUI.topping =
                donutsAndToppings.find {
                    it.donut.idDonut == donutUI.id
                }?.topping?.map { it.type }
                    .toString()
                    .removePrefix("[")
                    .removeSuffix("]")
                    .replace(",", "\n\t")
            mutableList.add(donutUI)
        }
        return mutableList
    }


    class Factory(private val repository: DonutsRepository) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListViewModel(repository) as T
        }
    }
}