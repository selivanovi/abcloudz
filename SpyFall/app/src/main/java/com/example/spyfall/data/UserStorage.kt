package com.example.spyfall.data

interface UserStorage {

    suspend fun getUserName(): String?

    suspend fun addUserName(name: String)
}