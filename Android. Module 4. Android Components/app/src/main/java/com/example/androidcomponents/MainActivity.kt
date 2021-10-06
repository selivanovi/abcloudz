package com.example.androidcomponents

import android.Manifest
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val serviceBinder: ServiceBinder = ServiceBinder()

    private lateinit var serviceIntent: Intent
    private lateinit var serviceConnection: ServiceConnection

    private val buttonSelectPicture: Button by lazy {
        findViewById<Button>(R.id.buttonSelectPicture)
    }

    private var producer: Producer? = null


    private val consumer = object : Consumer {
        override fun resultOnReady(list: ArrayList<String>) {
            buttonSelectPicture.isEnabled = true
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                serviceBinder.bind(this)
            } else {

            }
        }

    private fun event(list: ArrayList<String>) {
        buttonSelectPicture.isEnabled = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        buttonSelectPicture.isEnabled= false

        serviceBinder.setEvent = ::event

        buttonSelectPicture.setOnClickListener {
            startActivity(GalleryActivity.newIntent(this))
        }
    }

    override fun onStart() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        super.onStart()
    }

    override fun onStop() {
        serviceBinder.unbind(this)
        super.onStop()
    }
}