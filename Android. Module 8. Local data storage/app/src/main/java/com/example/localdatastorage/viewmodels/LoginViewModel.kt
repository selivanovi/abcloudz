package com.example.localdatastorage.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.localdatastorage.R
import com.example.localdatastorage.model.entities.json.DonutJson
import com.example.localdatastorage.model.room.DonutDataBase
import com.example.localdatastorage.model.room.DonutsRepository
import com.example.localdatastorage.model.room.entities.*
import com.example.localdatastorage.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    application: Application,
    private val sharedPreferences: SharedPreferences,
    private val donutsRepository: DonutsRepository
) : AndroidViewModel(application) {

    val isFirstLaunch: Boolean
        get() =
            if (sharedPreferences.getBoolean(IS_LAUNCH_FIRST, true)) {
                sharedPreferences.edit {
                    putBoolean(IS_LAUNCH_FIRST, false)
                }
                true
            } else
                false

    fun getEmail(): String? {
        return sharedPreferences.getString(EMAIL_KEY, null)
    }

    fun putEmail(email: String) {
        if (!email.isValidEmail()) throw ValidateException(
            getApplication<Application>().resources.getString(
                R.string.email_error
            )
        )
        sharedPreferences.edit {
            putString(EMAIL_KEY, email)
        }
    }

    fun getPassword(): String? {
        return sharedPreferences.getString(PASSWORD_KEY, null)
    }

    fun putPassword(password: String) {
        if (!password.isValidPassword()) throw ValidateException(
            getApplication<Application>().resources.getString(
                R.string.password_error
            )
        )
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

    fun loadDataInDataBase(donutsJson: List<DonutJson>) {

        val donuts = mutableListOf<Donut>()
        val toppings = mutableListOf<Topping>()
        val batters = mutableListOf<Batter>()
        val donutsAndToppings = mutableListOf<DonutToppingCrossRef>()
        val donutsAndBatters = mutableListOf<DonutBatterCrossRef>()

        donutsJson.forEach { donutJson ->
            donuts.add(donutJson.toDTO())
            toppings.addAll(donutJson.topping.map { it.toDTO() })
            batters.addAll(donutJson.batters.batter.map { it.toDTO() })
            donutsAndBatters.addAll(donutJson.toBatterRelation())
            donutsAndToppings.addAll(donutJson.toToppingRelation())
        }

        viewModelScope.launch(Dispatchers.IO) {
            donutsRepository.insertDonutsWithToppingsAndButters(
                donuts,
                batters,
                toppings,
                donutsAndBatters,
                donutsAndToppings
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        val application: Application
    ) : ViewModelProvider.Factory {

        private val masterKey =
            MasterKey.Builder(application.applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()


        private val sharedPreferences: SharedPreferences =
            EncryptedSharedPreferences.create(
                application.applicationContext,
                "secret_shared_preferences",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

        private val dataBase =
            DonutDataBase.getInstance(application.applicationContext)


        private val donutsRepository =
            DonutsRepository(dataBase)


        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(application, sharedPreferences, donutsRepository) as T
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