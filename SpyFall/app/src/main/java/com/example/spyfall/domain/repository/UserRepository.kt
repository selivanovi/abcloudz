package com.example.spyfall.domain.repository

interface UserRepository {

    suspend fun getUsers(): List<String>?
    suspend fun addUser(name: String)
}
