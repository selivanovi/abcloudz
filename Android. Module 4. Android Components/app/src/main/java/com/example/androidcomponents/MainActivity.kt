package com.example.androidcomponents

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcomponents.adapters.ImageAdapter
import com.example.androidcomponents.services.ScanImageService

class MainActivity : AppCompatActivity() {

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startActivity(GalleryActivity.newIntent(this))
            } else {

            }
        }

    private val buttonSelectPicture: Button by lazy {
        findViewById(R.id.buttonSelectPicture)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonSelectPicture.setOnClickListener {
//            requestPermission()
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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