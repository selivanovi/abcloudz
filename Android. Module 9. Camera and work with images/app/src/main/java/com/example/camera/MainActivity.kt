package com.example.camera

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.camera.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PhotoFragmentListener {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val getImageFromCamera = registerForActivityResult(CameraContract()) {
        binding.imageView.setImageURI(it)
    }

    private val getImageFromFiles = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.imageView.setImageURI(it)
        Log.d("MainActivity", it.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.takePhotoButton.setOnClickListener {
            PhotoDialogFragment().show(supportFragmentManager, null)
        }
        binding.addFilterButton.setOnClickListener {

        }
        binding.addEmojiButton.setOnClickListener {

        }
        binding.drawLineButton.setOnClickListener {
            imageView.isDrawable = true
        }
    }

    override fun onClickCamera() {
        getImageFromCamera.launch(null)
    }

    override fun onClickFiles() {
        getImageFromFiles.launch(MIMETYPE_FILTER)
    }

    companion object {
        private const val MIMETYPE_FILTER = "image/*"
    }
}