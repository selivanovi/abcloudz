package com.example.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PhotoViewModel : ViewModel() {

    private val _bitmapsChannel = Channel<List<Bitmap>>()
    val bitmapsChannel
        get() = _bitmapsChannel.receiveAsFlow()

    fun getFilteredBitmap(bitmap: Bitmap) {
        val filters = listOf<Filter>(GreyFilter(), BrightnessFilter(), SaturationFilter())
        val bitmaps = mutableListOf<Bitmap>()
        viewModelScope.launch {
            filters.forEach {
                bitmaps.add(it.from(bitmap))
            }
            _bitmapsChannel.send(bitmaps)
        }
    }
}