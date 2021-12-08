package com.example.camera.fragments.listeners

import android.graphics.Bitmap
import android.net.Uri

interface MenuFragmentListener {

    fun setImage(uri: Uri?)
    fun setColor(color: Int)
    fun addEmoji(drawable: Int)
    fun setFilter(filter: Bitmap)
    fun setDrawable(boolean: Boolean)
    fun setAddable(boolean: Boolean)
}
