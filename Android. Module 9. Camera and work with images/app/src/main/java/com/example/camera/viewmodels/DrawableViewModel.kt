package com.example.camera.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.URI

class DrawableViewModel : ViewModel() {

    private val _channelDrawable = Channel<Boolean>()
    val channelDrawable = _channelDrawable.receiveAsFlow()

    private val _channelColor = Channel<Int>()
    val channelColor = _channelColor.receiveAsFlow()

    private val _channelImageURI = Channel<Uri?>()
    val channelImageURI = _channelImageURI.receiveAsFlow()

    private val _channelEmoji = Channel<Int>()
    val channelEmoji = _channelEmoji.receiveAsFlow()

    private val _channelFilter = Channel<Int>()
    val channelFilter = _channelEmoji.receiveAsFlow()

    fun emitColor(color: Int) {
        viewModelScope.launch {
            _channelColor.send(color)
        }
    }

    fun emitImageUri(uri: Uri?) {
        viewModelScope.launch {
            _channelImageURI.send(uri)
        }
    }

    fun emitDrawable(boolean: Boolean) {
        viewModelScope.launch {
            _channelDrawable.send(boolean)
        }
    }

    fun getFilters(): List<GPUImageFilter> {
        val filters = mutableListOf<GPUImageFilter>()

        filters.add(GPUImageAddBlendFilter())
        filters.add(GPUImageAlphaBlendFilter())
        filters.add(GPUImage3x3ConvolutionFilter())
        filters.add(GPUImageBrightnessFilter())
        filters.add(GPUImageLaplacianFilter())
        filters.add(GPUImageBulgeDistortionFilter())
        filters.add(GPUImageHueBlendFilter())

        return filters
    }
}