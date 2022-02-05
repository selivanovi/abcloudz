package com.example.spyfall.domain.repository

import com.example.spyfall.domain.entity.UserDomain
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun addUser(userDomain: UserDomain): Flow<Result<Unit?>>

    fun getUser(): UserDomain?

    suspend fun deleteUser(userDomain: UserDomain)
}
