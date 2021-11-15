package com.example.networking

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import kotlinx.coroutines.*
import java.io.IOException
import java.net.URL

fun getBitmapFromUrl(url: String): Bitmap? =
    try {
        val connection = URL(url).openStream()
        BitmapFactory.decodeStream(connection)
    } catch (e: IOException) {
        null
    }


fun setImageFromUrl(url: String?, imageView: ImageView) = CoroutineScope(Dispatchers.IO).launch {
    if (url == null) {
        imageView.setImageBitmap(null)

    } else {
        val bitmapDeferred: Deferred<Bitmap?> = async { getBitmapFromUrl(url) }
        withContext(Dispatchers.Main) {
            imageView.setImageBitmap(null)
        }
        val bitmap = bitmapDeferred.await()
        withContext(Dispatchers.Main) {
            imageView.setImageBitmap(bitmap)
        }
    }
}