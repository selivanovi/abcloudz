package com.example.spyfall.data.remote

import com.example.spyfall.data.RemoteDataSource
import com.example.spyfall.data.utils.Constants
import com.example.spyfall.data.utils.GetDataException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class FirebaseSource @Inject constructor(
    private val firebaseDataBase: FirebaseDatabase
) : RemoteDataSource {

    override suspend fun getUsers(): List<String>? {
        var users: MutableList<String>? = null
        firebaseDataBase.getReference("users").addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    users = mutableListOf()
                    for (postSnapshot in snapshot.children) {
                        users!!.add(postSnapshot.getValue(String.javaClass).toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    throw GetDataException(Constants.GET_DATA_EXCEPTION)
                }
            }
        )
        return users
    }

    override suspend fun addUser(name: String) {
        firebaseDataBase.getReference("users").setValue(name)
    }
}