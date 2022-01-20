package com.example.spyfall.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.spyfall.data.utils.Constants
import com.example.spyfall.data.utils.GetDataException
import com.example.spyfall.data.utils.InvalidNameException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val sharedPreferences: SharedPreferences
) : UserRepository {

    override suspend fun addUserName(name: String) = callbackFlow<Result<Unit?>> {

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.map { it.getValue(String::class.java) }
                if (!users.contains(name)) {
                    sharedPreferences.edit {
                        putString(KEY_USER_NAME, name)
                    }
                    firebaseDatabase.reference.child(USER_REFERENCES).child(name).setValue(name)
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

    override fun getUserName(): String? =
        sharedPreferences.getString(KEY_USER_NAME, null)

    override suspend fun deleteName(name: String) {
        sharedPreferences.edit{
            putString(KEY_USER_NAME, null)
        }
        firebaseDatabase.reference.child(USER_REFERENCES).child(name).setValue(null)
    }

    companion object {
        private const val KEY_USER_NAME = "key_user_name"
        private const val USER_REFERENCES = "users"
    }
}