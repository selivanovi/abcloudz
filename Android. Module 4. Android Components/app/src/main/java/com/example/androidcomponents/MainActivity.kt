package com.example.androidcomponents

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.GridLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcomponents.adapters.ImageAdapter

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