package com.example.camera

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import kotlinx.coroutines.*

abstract class Filter {

    abstract fun from(bitmap: Bitmap): Bitmap

    protected fun createNewBitmap(
        oldBitmap: Bitmap,
        pixelTransformation: (oldRed: Int, oldBlue: Int, oldGreen: Int, alpha: Int) -> Pixels
    ): Bitmap {

        val newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        // height and width of Image
        val imageHeight = newBitmap.height
        val imageWidth = newBitmap.width
        Log.e("Image Size", "Height=" + imageHeight + " Width=" + imageWidth);
        // traversing each pixel in Image as an 2D Array
        for (i in 0 until imageWidth) {

            for (j in 0 until imageHeight) {

                // getting each pixel
                val oldPixel = oldBitmap.getPixel(i, j);

                // each pixel is made from RED_BLUE_GREEN_ALPHA
                // so, getting current values of pixel
                val oldRed = Color.red(oldPixel)
                val oldGreen = Color.green(oldPixel)
                val oldBlue = Color.blue(oldPixel)
                val oldAlpha = Color.alpha(oldPixel)

                val pixels = pixelTransformation(oldRed, oldGreen, oldBlue, oldAlpha)
                // write your Algorithm for getting new values
                // after calculation of filter


                // applying new pixel values from above to newBitmap
                val newPixel = Color.argb(oldAlpha, pixels.red, pixels.green, pixels.blue);
                newBitmap.setPixel(i, j, newPixel)
            }
        }
        return newBitmap
    }
}

class OriginalFilter() : Filter() {
    private val _bitmap: Deferred<Bitmap>? = null

    override fun from(bitmap: Bitmap): Bitmap {
        return super.createNewBitmap(bitmap, ::transformPixels)
    }

    private fun transformPixels(
        oldRed: Int,
        oldGreen: Int,
        oldBlue: Int,
        oldAlpha: Int
    ): Pixels {

        return Pixels(oldRed, oldGreen, oldBlue)
    }

    private fun truncate(x: Int): Int {
        return Math.min(255, Math.max(0, x))
    }
}

class SaturationFilter() : Filter() {
    private val _bitmap: Deferred<Bitmap>? = null

    override fun from(bitmap: Bitmap): Bitmap {
        return super.createNewBitmap(bitmap, ::transformPixels)
    }

    private fun transformPixels(
        oldRed: Int,
        oldGreen: Int,
        oldBlue: Int,
        oldAlpha: Int
    ): Pixels {
        val intensity: Int = (oldRed + oldBlue + oldGreen) / 3
        val newRed = truncate(oldAlpha * (oldRed - intensity) + intensity)
        val newGreen = truncate(oldAlpha * (oldGreen - intensity) + intensity)
        val newBlue = truncate(oldAlpha * (oldBlue - intensity) + intensity)

        return Pixels(newRed, newGreen, newBlue)
    }

    private fun truncate(x: Int): Int {
        return Math.min(255, Math.max(0, x))
    }
}

class BrightnessFilter() : Filter() {
    private var _bitmap: Bitmap? = null

    override fun from(bitmap: Bitmap): Bitmap {
        return super.createNewBitmap(bitmap, ::transformPixels)
    }

    private fun transformPixels(
        oldRed: Int,
        oldGreen: Int,
        oldBlue: Int,
        oldAlpha: Int
    ): Pixels {
        val intensity: Int = (oldRed + oldBlue + oldGreen) / 3
        val newRed = truncate(oldRed + intensity)
        val newGreen = truncate(oldGreen + intensity)
        val newBlue = truncate(oldBlue + intensity)

        return Pixels(newRed, newGreen, newBlue)
    }

    private fun truncate(x: Int): Int {
        return Math.min(255, Math.max(0, x))
    }
}

class GreyFilter() : Filter() {
    private var _bitmap: Deferred<Bitmap>? = null

    override fun from(bitmap: Bitmap): Bitmap {
        return super.createNewBitmap(bitmap, ::transformPixels)
    }


    private fun transformPixels(oldRed: Int, oldGreen: Int, oldBlue: Int, alpha: Int): Pixels {
        val intensity: Int = (oldRed + oldBlue + oldGreen) / 3

        return Pixels(intensity, intensity, intensity)
    }

}

class Pixels(val red: Int, val green: Int, val blue: Int)
