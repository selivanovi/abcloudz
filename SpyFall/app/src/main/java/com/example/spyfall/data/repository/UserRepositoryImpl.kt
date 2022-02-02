package com.example.spyfall.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.spyfall.domain.entity.User
import com.example.spyfall.data.utils.Constants
import com.example.spyfall.data.utils.GetDataException
import com.example.spyfall.data.utils.InvalidNameException
import com.example.spyfall.domain.repository.UserRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val sharedPreferences: SharedPreferences
) : UserRepository {

    override suspend fun addUser(user: User) = callbackFlow<Result<Unit?>> {

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.map { it.getValue(String::class.java) }
                if (!users.contains(user.name)) {
                    sharedPreferences.edit {
                        putString(KEY_USER_ID, user.userId)
                        putString(KEY_USER_NAME, user.name)
                    }
                    firebaseDatabase.reference.child(USER_REFERENCES).child(user.userId).setValue(user.name)
                    this@callbackFlow.trySendBlocking(Result.success(Unit))
                } else {
                    this@callbackFlow.trySendBlocking(Result.failure(InvalidNameException(Constants.INVALID_NAME_EXCEPTION)))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(GetDataException(Constants.GET_DATA_EXCEPTION)))
            }

        }

        firebaseDatabase.reference.child(USER_REFERENCES).addListenerForSingleValueEvent(
            valueEventListener
        )

        awaitClose {
            firebaseDatabase.getReference(USER_REFERENCES).removeEventListener(valueEventListener)
        }
    }

    override fun getUser(): User? {
        val userID = sharedPreferences.getString(KEY_USER_ID, null)
        val name = sharedPreferences.getString(KEY_USER_NAME, null)
        if (userID != null && name != null) {
            return User(userID, name)
        }
        return null
    }

    override suspend fun deleteUser(user: User) {
        sharedPreferences.edit {
            putString(KEY_USER_ID, null)
            putString(KEY_USER_NAME, null)
        }
        firebaseDatabase.reference.child(USER_REFERENCES).child(user.userId).setValue(null)
    }

    companion object {
        private const val KEY_USER_ID = "key_user_id"
        private const val KEY_USER_NAME = "key_user_name"
        private const val USER_REFERENCES = "users"
    }
}