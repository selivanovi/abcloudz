package com.example.androidcomponents

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcomponents.adapters.ImageAdapter

class GalleryActivity : AppCompatActivity() {

    private val TAG = "GalleryActivity"

    private var broadcastReceiver: BroadcastReceiver? = null

    private val serviceBinder: ServiceBinder = ServiceBinder()

    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.imageRecyclerView)
    }
    private val imageAdapter: ImageAdapter = ImageAdapter()


    private fun event(list: ArrayList<String>) {
        imageAdapter.setData(list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        serviceBinder.setEvent= ::event

        setContentView(R.layout.activity_gallery)
        createRecyclerView()
    }

    override fun onStart() {
        serviceBinder.bind(this)
        super.onStart()
    }

    override fun onStop() {
        serviceBinder.unbind(this)
        super.onStop()

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