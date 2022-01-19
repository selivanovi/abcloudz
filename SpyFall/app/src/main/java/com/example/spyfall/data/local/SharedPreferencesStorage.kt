package com.example.spyfall.data.local

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.spyfall.data.UserStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : UserStorage {

    override suspend fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }

    @SuppressLint("CommitPrefEdits")
    override suspend fun addUserName(name: String) {
        sharedPreferences.edit().putString(KEY_USER_NAME, name)
    }

    companion object {
        private const val KEY_USER_NAME = "key_user_name"
    }
}