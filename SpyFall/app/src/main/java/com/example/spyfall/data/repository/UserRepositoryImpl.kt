package com.example.spyfall.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.spyfall.domain.entity.UserDomain
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.utils.UserExistException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val sharedPreferences: SharedPreferences
) : UserRepository {

    override suspend fun createUser(name: String): UserDomain {
        val dataReference = firebaseDatabase.reference.child(USER_REFERENCES)

        val names = dataReference.get()
            .await().children.mapNotNull { snapshot ->
                snapshot.getValue(String::class.java)
            }

        if (names.contains(name)) {
            throw UserExistException(name)
        } else {
            val newUser = UserDomain(name = name)
            dataReference.child(newUser.userId).setValue(name)
            sharedPreferences.edit{
                putString(KEY_USER_NAME, newUser.name)
                putString(KEY_USER_ID, newUser.userId)
            }
            return newUser
        }
    }

    override fun getUser(): UserDomain? {
        val userID = sharedPreferences.getString(KEY_USER_ID, null)
        val name = sharedPreferences.getString(KEY_USER_NAME, null)
        if (userID != null && name != null) {
            return UserDomain(userID, name)
        }
        return null
    }

    override suspend fun deleteUser(userDomain: UserDomain) {
        sharedPreferences.edit {
            remove(KEY_USER_ID)
            remove(KEY_USER_NAME)
        }
        firebaseDatabase.reference.child(USER_REFERENCES)
            .child(userDomain.userId).removeValue()
    }

    companion object {
        private const val KEY_USER_ID = "key_user_id"
        private const val KEY_USER_NAME = "key_user_name"
        private const val USER_REFERENCES = "users"
    }
}
