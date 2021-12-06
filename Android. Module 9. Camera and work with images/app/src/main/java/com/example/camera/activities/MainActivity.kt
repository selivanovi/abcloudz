package com.example.camera.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.camera.activities.contracts.CameraContract
import com.example.camera.fragments.dialogs.PhotoDialogFragment
import com.example.camera.PhotoFragmentListener
import com.example.camera.R
import com.example.camera.databinding.ActivityMainBinding
import com.example.camera.fragments.PhotoFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .add(R.id.mainContainerView, PhotoFragment(), null)
            .commit()
    }
}