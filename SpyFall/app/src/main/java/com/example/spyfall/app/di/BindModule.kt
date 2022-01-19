package com.example.spyfall.app.di

import com.example.spyfall.data.RemoteDataSource
import com.example.spyfall.data.UserStorage
import com.example.spyfall.data.local.SharedPreferencesStorage
import com.example.spyfall.data.remote.FirebaseSource
import com.example.spyfall.data.repository.UserRepositoryImpl
import com.example.spyfall.domain.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
interface BindModule {

    @Binds
    fun bindSharedPreferencesStorageToUserStorage(
        sharedPreferencesStorage: SharedPreferencesStorage
    ) : UserStorage

    @Binds
    fun bindUserRepositoryImplToUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ) : UserRepository

    @Binds
    fun bindFirebaseSourceToRemoteDataSource(
        firebaseSource: FirebaseSource
    ) : RemoteDataSource
}