package com.example.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val liveData = MutableLiveData<String>()

    fun changeTitle(title: String) {
        liveData.postValue(title)
    }
}