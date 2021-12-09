package com.example.camera.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*

import android.app.Application
import android.content.ContentValues
import android.graphics.BitmapFactory
import androidx.lifecycle.*

import android.os.Build
import android.os.Environment
import android.provider.MediaStore

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream



class DrawableViewModel(context: Application) : AndroidViewModel(context) {

    private var originalBitmap: Bitmap? = null

    private val _channelDrawable = MutableLiveData<Boolean>()
    val channelDrawable: LiveData<Boolean> = _channelDrawable

    private val _channelColor = MutableLiveData<Int>()
    val channelColor: LiveData<Int> = _channelColor

    private val _channelImageBitmap = MutableLiveData<Bitmap?>()
    val channelImageBitmap: LiveData<Bitmap?> = _channelImageBitmap

    private val _channelEmoji = MutableLiveData<Int>()
    val channelEmoji: LiveData<Int> = _channelEmoji

    private val _channelAddable = MutableLiveData<Boolean>()
    val channelAddable: LiveData<Boolean> = _channelAddable

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

    fun saveBitmapToStorage(bitmap: Bitmap) {
        val context = getApplication<Application>()
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }

    companion object {
        private const val TAG = "DrawableViewModel"
    }
}