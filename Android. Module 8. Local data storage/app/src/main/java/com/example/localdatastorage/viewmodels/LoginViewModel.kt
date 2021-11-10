package com.example.localdatastorage.viewmodels

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.localdatastorage.R
import com.example.localdatastorage.utils.validateEmail
import com.example.localdatastorage.utils.validatePassword

class LoginViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

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
        if (!validateEmail(email)) error(R.string.email_error)
        sharedPreferences.edit {
            putString(EMAIL_KEY, email)
        }
    }

    fun getPassword(): String {
        return sharedPreferences.getString(PASSWORD_KEY, null) ?: throw NullPointerException()
    }

    fun putPassword(password: String) {
        if (!validatePassword(password)) error(R.string.password_error)
        sharedPreferences.edit {
            putString(PASSWORD_KEY, password)
        }
    }

    fun putStateOfFingerPrint(state: Boolean) {
        sharedPreferences.edit {
            putBoolean(STATE_OF_FINGERPRINT_KEY, state)
        }
    }

    fun getStateOfFingerPrint(): Boolean {
        return sharedPreferences.getBoolean(STATE_OF_FINGERPRINT_KEY, true)
    }

    fun loadDataInDataBase() {

    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(sharedPreferences) as T
        }
    }

    companion object {
        const val TAG = "LoginViewModels"

        private const val IS_LAUNCH_FIRST = "isLaunchFirst"
        private const val PASSWORD_KEY = "password_key"
        private const val STATE_OF_FINGERPRINT_KEY = "finger_print_key"
        private const val EMAIL_KEY = "email_key"
    }
}