package com.example.camera.activities.contracts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.net.toUri
import com.example.camera.activities.CameraActivity

class CameraContract : ActivityResultContract<Unit, Uri?>() {
    override fun createIntent(context: Context, input: Unit?): Intent {
        return CameraActivity.getIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        intent ?: return null
        if (resultCode != CameraActivity.RESULT_SUCCESSFUL) return null

        val uri = intent.getStringExtra(CameraActivity.IMAGE_URI)?.toUri()
        Log.d(TAG, "Uri: $uri")
        return uri
    }

    companion object {
        private const val TAG = "CameraContract"
    }
}