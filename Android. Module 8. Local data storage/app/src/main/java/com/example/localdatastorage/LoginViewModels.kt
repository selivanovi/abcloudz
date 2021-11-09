package com.example.localdatastorage

import android.content.SharedPreferences
import android.security.keystore.KeyProperties
import android.util.Log
import android.view.Display
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.localdatastorage.utils.CryptographyUtil
import java.nio.charset.Charset
import javax.crypto.Cipher

class LoginViewModels(private val sharedPreferences: SharedPreferences) : ViewModel() {

    val isFirstLaunch: Boolean
        get() =
            if (sharedPreferences.getBoolean(IS_LAUNCH_FIRST, true)) {
                sharedPreferences.edit {
                    putBoolean(IS_LAUNCH_FIRST, false)
                }
                true
            } else
                false

    fun getEmail(): String {
        return sharedPreferences.getString(EMAIL_KEY, null) ?: throw NullPointerException()
    }

    fun putEmail(email: String) {
        sharedPreferences.edit {
            putString(EMAIL_KEY, email)
        }
    }

    fun getPassword(): String {
        return sharedPreferences.getString(PASSWORD_KEY, null) ?: throw NullPointerException()
    }

    fun putPassword(password: String) {
        sharedPreferences.edit {
            putString(PASSWORD_KEY, password)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModels(sharedPreferences) as T
        }
    }

    companion object {
        const val TAG = "LoginViewModels"

        private const val IS_LAUNCH_FIRST = "isLaunchFirst"
        private const val PASSWORD_KEY = "password_key"
        private const val EMAIL_KEY = "email_key"
    }
}