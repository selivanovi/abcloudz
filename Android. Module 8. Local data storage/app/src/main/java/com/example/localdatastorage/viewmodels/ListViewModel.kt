package com.example.localdatastorage.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListViewModel(
//    private val repository: DonutsListRepository
) {




    class Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListViewModel() as T
        }
    }
}