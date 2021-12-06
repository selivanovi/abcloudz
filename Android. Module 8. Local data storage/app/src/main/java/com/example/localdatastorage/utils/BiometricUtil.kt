package com.example.localdatastorage.utils

import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.localdatastorage.R
import com.example.localdatastorage.dialogfragments.BiometricDialogFragment

private const val TAG = "BiometricUtil"

object BiometricUtil {

    fun show(fragment: Fragment, setSucceed: () -> Unit, setError: () -> Unit) {
        val biometricManager = BiometricManager.from(fragment.requireActivity())
        when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        ) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d(TAG, "App can authenticate using biometrics.")
                create(fragment, setSucceed)
                    .authenticate(
                        createPromptInfo(fragment),
                    )
                
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e(TAG, "No biometric features available on this device.")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e(TAG, "Biometric features are currently unavailable.")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e(TAG, "Biometric ")
                setError.invoke()
            }
        }
    }

    private fun create(fragment: Fragment, setSucceed: () -> Unit): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(fragment.requireContext())

        val callback =
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Log.d(TAG, "$errorCode :: $errString")
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.d(TAG, "Authentication failed for an unknown reason")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.d(TAG, "Authentication was successful")
                    setSucceed.invoke()
                }
            }

        return BiometricPrompt(fragment, executor, callback)
    }

    private fun createPromptInfo(fragment: Fragment): BiometricPrompt.PromptInfo {

        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(fragment.getString(R.string.prompt_info_title))
            .setSubtitle(fragment.getString(R.string.prompt_info_subtitle))
            .setNegativeButtonText(fragment.getString(R.string.prompt_info_cancel))
            .build()
    }
}