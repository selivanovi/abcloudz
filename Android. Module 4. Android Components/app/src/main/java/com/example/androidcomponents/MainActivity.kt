package com.example.androidcomponents

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val buttonSelectPicture: Button by lazy {
        findViewById(R.id.buttonSelectPicture)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonSelectPicture.setOnClickListener {
            requestPermission()
            startActivity(GalleryActivity.newIntent(this))
        }
    }

    private fun hasReadExternalStoragePermissions() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        val permission = mutableListOf<String>()

        if(!hasReadExternalStoragePermissions())
            permission.add(Manifest.permission.READ_EXTERNAL_STORAGE)


        if(permission.size != 0)
            ActivityCompat.requestPermissions(this, permission.toTypedArray(), 0)

    }
}