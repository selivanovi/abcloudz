package com.example.spyfall.domain.repository

import com.example.spyfall.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun addUser(user: User): Flow<Result<Unit?>>

    fun getUser(): User?

    suspend fun deleteUser(user: User)
}
