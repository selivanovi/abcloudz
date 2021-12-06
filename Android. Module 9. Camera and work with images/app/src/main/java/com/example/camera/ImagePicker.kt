package com.example.camera

import android.net.Uri
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner

class ImagePicker(
    private val activityResultRegistry: ActivityResultRegistry,
    private val lifecycleOwner: LifecycleOwner,
    private val callback: (imageUri: Uri) -> Unit,
) {

    private val getContent: ActivityResultLauncher<String> =
        activityResultRegistry.register(
            REGISTRY_KEY,
            ActivityResultContracts.GetContent(),
            callback
        )

    fun pickImage() {
        getContent.launch(MIMETYPE_FILTER)
    }

    companion object {
        private const val MIMETYPE_FILTER = "image/*"
        private const val REGISTRY_KEY = "ImagePicker"
    }
}