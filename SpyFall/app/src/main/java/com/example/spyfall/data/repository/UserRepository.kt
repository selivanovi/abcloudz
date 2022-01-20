package com.example.spyfall.data.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun addUserName(name: String): Flow<Result<Unit?>>

    fun getUserName(): String?

    suspend fun deleteName(name: String)
}
