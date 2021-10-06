package com.example.androidcomponents.services

import android.app.Service
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import com.example.androidcomponents.Producer
import com.example.androidcomponents.Consumer
import kotlinx.coroutines.*

class ScanImageService : Service() {

    private val TAG = "MyService"

    private val consumerAggregator = ConsumerAggregator()

    override fun onCreate() {
        Log.d(TAG, "MyService onCreate")
        super.onCreate()
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.d(TAG, "MyService onBind")
        return MyBinder();
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "MyService onUnbind")
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        Log.d(TAG, "MyService onRebind")
        super.onRebind(intent)
    }

    override fun onDestroy() {
        Log.d(TAG, "MyService onDestroy")
        super.onDestroy()
    }

    inner class MyBinder : Binder(), Producer {
        override fun addObserver(consumer: Consumer) {
            consumerAggregator.consumers.add(consumer)
        }

        override fun removeObserver(consumer: Consumer) {
            consumerAggregator.consumers.remove(consumer)
        }

        override fun scanData() {
            startScanning()
        }
    }

    private fun startScanning() {
        var result: ArrayList<String>? = null
        Thread {
                Log.d(TAG, "Create new thread")
                result = getImagePath()
            result?.let {
                CoroutineScope(Dispatchers.Main).launch { sendResult(it) }
            }

        }.start()

    }

    fun sendResult(arrayList: ArrayList<String>) =
                consumerAggregator.resultOnReady(arrayList)


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

    inner class ConsumerAggregator: Consumer {

        val consumers = mutableListOf<Consumer>()

        override fun resultOnReady(list: ArrayList<String>) {
            consumers.forEach {
                it.resultOnReady(list)
            }
        }

    }

    companion object {

        fun newIntent(context: Context) =
            Intent(context, ScanImageService::class.java)
    }

}