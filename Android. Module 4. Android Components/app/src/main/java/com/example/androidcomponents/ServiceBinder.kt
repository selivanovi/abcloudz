package com.example.androidcomponents

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcomponents.services.ScanImageService

class ServiceBinder() {

    private val TAG = "ServiceBinder"

    var setEvent: ((ArrayList<String>) -> Unit)? = null

    private var producer: Producer? = null

    private var serviceIntent: Intent? = null

    private val serviceConnection: ServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            Log.d(TAG, "ServiceBinder onServiceConnected")
            producer = binder as Producer
            producer?.addObserver(consumer)
            producer?.scanData()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d(TAG, "Service onServiceDisconnected")
            disconnectProducer()
        }
    }

    fun bind(context: Context) {
        serviceIntent = ScanImageService.newIntent(context)
        context.bindService(serviceIntent, serviceConnection, AppCompatActivity.BIND_AUTO_CREATE)
    }

    fun unbind(context: Context) {
        context.unbindService(serviceConnection)
    }

    private fun disconnectProducer() {
        producer?.removeObserver(consumer)
        producer = null
    }


    private val consumer = object : Consumer {
        override fun resultOnReady(list: ArrayList<String>) {
            setEvent?.let {
                it.invoke(list)
            }
        }
    }
}