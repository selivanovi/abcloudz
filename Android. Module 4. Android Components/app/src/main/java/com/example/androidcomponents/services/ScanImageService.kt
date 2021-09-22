package com.example.androidcomponents.services

import android.app.Service
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import com.example.androidcomponents.GalleryActivity

class ScanImageService : Service() {

    private val TAG = "ScanImageService"

    override fun onBind(p0: Intent?): IBinder?  = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service is starting")
        startScanning()
        return START_STICKY
    }

    private fun startScanning() {
        Thread {
            Log.d(TAG, "Create new thread")
            val result = getImagePath()
            sendResult(result)
        }.start()
    }

    private fun getImagePath(): ArrayList<String> {
        val result = arrayListOf<String>()
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)
        contentResolver.query(uri, projection, null, null, null)?.use {
            while (it.moveToNext()) {
                result.add(
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        it.getLong(0)
                    ).toString()
                )
            }
        }
        return result
    }

    private fun sendResult(result: ArrayList<String>) {
        val intent = Intent(GalleryActivity.IMAGES_LOADED)
        intent.putStringArrayListExtra(GalleryActivity.PARAM_RESULT, result)
        sendBroadcast(intent)
        Log.d(TAG, "Send result: $result")
    }

    companion object {
        fun newIntent(context: Context) =
            Intent(context, ScanImageService::class.java)
    }
}