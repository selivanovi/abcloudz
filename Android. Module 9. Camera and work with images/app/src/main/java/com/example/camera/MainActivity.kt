package com.example.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.camera.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val getImageFromCamera = registerForActivityResult(CameraContract()) {
        binding.imageView.setImageURI(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.takePhotoButton.setOnClickListener {
            getImageFromCamera.launch(null)
        }
    }

    companion object {

    }
}