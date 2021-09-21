package com.example.androidcomponents

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcomponents.adapters.ImageAdapter

class GalleryActivity : AppCompatActivity() {

    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.imageRecyclerView)
    }
    private val imageAdapter: ImageAdapter = ImageAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        createRecyclerView(getImagePath())
    }

    private fun createRecyclerView(images: List<Uri>) {
        imageAdapter.setData(images)
        recyclerView.adapter = imageAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 3)
    }

    private fun getImagePath(): List<Uri> {
        val result = mutableListOf<Uri>()
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)
        contentResolver.query(uri, projection, null, null, null)?.use {
            while (it.moveToNext()) {
                result.add(
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        it.getLong(0)
                    )
                )
            }
        }
        return result
    }

    companion object {
        fun newIntent(context: Context) =
            Intent(context, GalleryActivity::class.java)
    }
}