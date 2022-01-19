package com.example.spyfall.data.repository

import com.example.spyfall.data.UserStorage
import com.example.spyfall.data.RemoteDataSource
import com.example.spyfall.domain.repository.UserRepository

class UserRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val userStorage: UserStorage
) : UserRepository {

    override suspend fun getUsers(): List<String>? =
        remoteDataSource.getUsers()

    override suspend fun addUser(name: String) {
        remoteDataSource.addUser(name)
        userStorage.addUserName(name)
    }
}