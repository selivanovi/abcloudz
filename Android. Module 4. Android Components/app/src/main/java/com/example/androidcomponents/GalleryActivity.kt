package com.example.androidcomponents

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcomponents.adapters.ImageAdapter
import com.example.androidcomponents.services.ScanImageService

class GalleryActivity : AppCompatActivity() {

    private val TAG = "GalleryActivity"

    private var broadcastReceiver: BroadcastReceiver? = null

    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.imageRecyclerView)
    }
    private val imageAdapter: ImageAdapter = ImageAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        startService(ScanImageService.newIntent(this))
        createRecyclerView()
        createReceiver()
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        stopService(ScanImageService.newIntent(this))
        super.onDestroy()
    }

    private fun createReceiver() {

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                val result = intent?.getStringArrayListExtra(PARAM_RESULT)
                Log.d(TAG, "Get data: $result")
                result?.let {
                    imageAdapter.setData(it)
                }

            }
        }
        val intFilt = IntentFilter(IMAGES_LOADED);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(broadcastReceiver, intFilt)

        Log.d(TAG, "Create receiver")
    }

    private fun createRecyclerView() {
        recyclerView.adapter = imageAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 3)
    }

    companion object {
        const val IMAGES_LOADED = "com.example.androidcomponents.IMAGES_LOADED"
        const val PARAM_RESULT = "result"

        fun newIntent(context: Context) =
            Intent(context, GalleryActivity::class.java)
    }
}