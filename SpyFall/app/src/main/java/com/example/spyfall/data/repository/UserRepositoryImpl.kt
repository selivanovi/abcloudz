package com.example.spyfall.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.spyfall.domain.entity.UserDomain
import com.example.spyfall.domain.repository.UserRepository
import com.example.spyfall.utils.Constants
import com.example.spyfall.utils.DatabaseNotResponding
import com.example.spyfall.utils.InvalidNameException
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


    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override suspend fun addUser(userDomain: UserDomain) =
        callbackFlow<Result<Unit?>> {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.children.map { it.getValue(String::class.java) }
                    if (!users.contains(userDomain.name)) {
                        sharedPreferences.edit {
                            putString(KEY_USER_ID, userDomain.userId)
                            putString(KEY_USER_NAME, userDomain.name)
                        }
                        firebaseDatabase.reference.child(USER_REFERENCES)
                            .child(userDomain.userId).setValue(userDomain.name)
                        trySendBlocking(Result.success(Unit))
                    } else {
                        trySendBlocking(
                            Result.failure(InvalidNameException())
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySendBlocking(
                        Result.failure(DatabaseNotResponding())
                    )
                }
            }

            firebaseDatabase.reference.child(USER_REFERENCES)
                .addListenerForSingleValueEvent(valueEventListener)

            awaitClose {
                firebaseDatabase.getReference(USER_REFERENCES)
                    .removeEventListener(valueEventListener)
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
