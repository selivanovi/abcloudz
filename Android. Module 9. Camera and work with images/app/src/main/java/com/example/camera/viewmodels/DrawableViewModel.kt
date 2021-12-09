package com.example.camera.viewmodels

import android.R.attr
import android.graphics.Bitmap
import android.net.Uri
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.URI
import android.provider.MediaStore

import android.R.attr.data
import android.app.Application
import android.content.Context
import android.graphics.BitmapFactory
import androidx.lifecycle.*
import java.io.InputStream
import androidx.test.core.app.ApplicationProvider.getApplicationContext

import android.content.ContextWrapper
import com.example.camera.activities.CameraActivity
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class DrawableViewModel(context: Application) : AndroidViewModel(context) {

    private var originalBitmap: Bitmap? = null

    private val _channelDrawable = MutableLiveData<Boolean>()
    val channelDrawable: LiveData<Boolean> = _channelDrawable

    private val _channelColor = MutableLiveData<Int>()
    val channelColor: LiveData<Int>  = _channelColor

    private val _channelImageBitmap = MutableLiveData<Bitmap?>()
    val channelImageBitmap: LiveData<Bitmap?>  = _channelImageBitmap

    private val _channelEmoji = MutableLiveData<Int>()
    val channelEmoji: LiveData<Int>  = _channelEmoji

    private val _channelAddable = MutableLiveData<Boolean>()
    val channelAddable: LiveData<Boolean>  = _channelAddable

    fun emitColor(color: Int) {
        _channelColor.value = color
    }

    fun emitImageBitmap(bitmap: Bitmap?) {
        _channelImageBitmap.value = bitmap
    }

    fun emitDrawable(boolean: Boolean) {
        _channelDrawable.value = boolean
    }

    fun emitEmoji(drawable: Int) {
        _channelEmoji.value = drawable
    }

    fun emitAddable(boolean: Boolean) {
        _channelAddable.value = boolean
    }

    fun emitOriginalBitmap(){
        _channelImageBitmap.value = originalBitmap
    }

    fun getBitmapFromUri(imageUri: Uri): Bitmap? {
        getApplication<Application>().contentResolver.openInputStream(imageUri)?.let {
            originalBitmap = BitmapFactory.decodeStream(it)
            return originalBitmap
        }
        return null
    }

    fun getFilters(): List<Bitmap> {
        val gpuImage = GPUImage(getApplication())
        gpuImage.setImage(originalBitmap)
        val filters = mutableListOf<Bitmap>()

        GPUImageFilter().also {
            gpuImage.setFilter(it)
            filters.add(gpuImage.bitmapWithFilterApplied)
        }

        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.2f, 0.0f,
                0.1f, 0.1f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f
            )
        ).also {
            gpuImage.setFilter(it)
            filters.add(gpuImage.bitmapWithFilterApplied)
        }

        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.4f, 0.6f, 0.5f, 0.0f,
                0.0f, 0.4f, 1.0f, 0.0f,
                0.05f, 0.1f, 0.4f, 0.4f,
                1.0f, 1.0f, 1.0f, 1.0f
            )
        ).also {
            gpuImage.setFilter(it)
            filters.add(gpuImage.bitmapWithFilterApplied)
        }

        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.25f, 0.0f, 0.2f, 0.0f,
                0.0f, 1.0f, 0.2f, 0.0f,
                0.0f, 0.3f, 1.0f, 0.3f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also {
            gpuImage.setFilter(it)
            filters.add(gpuImage.bitmapWithFilterApplied)
        }

        GPUImageMonochromeFilter().also {
            gpuImage.setFilter(it)
            filters.add(gpuImage.bitmapWithFilterApplied)
        }

        GPUImageLuminanceFilter().also {
            gpuImage.setFilter(it)
            filters.add(gpuImage.bitmapWithFilterApplied)
        }

        return filters
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(cw)
        // path to /data/data/yourapp/app_data/imageDir
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(CameraActivity.FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + "-edit.jpg"
        )
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}