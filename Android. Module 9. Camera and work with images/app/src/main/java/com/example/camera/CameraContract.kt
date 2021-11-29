package com.example.camera

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.net.toUri

class CameraContract : ActivityResultContract<Unit, Uri?>() {
    override fun createIntent(context: Context, input: Unit?): Intent {
        return CameraActivity.getIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (intent == null) return null
        if (resultCode != CameraActivity.RESULT_SUCCESSFUL) return null

        val uri = intent.getStringExtra(CameraActivity.IMAGE_URI)?.toUri()
        Log.d(TAG, "Uri: $uri")
        return uri
    }

    companion object {
        private const val TAG = "CameraContract"
    }
}