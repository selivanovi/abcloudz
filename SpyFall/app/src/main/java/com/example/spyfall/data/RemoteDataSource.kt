package com.example.spyfall.data

interface RemoteDataSource {

    suspend fun getUsers(): List<String>?

    suspend fun addUser(name: String)
}