package com.example.spyfall.app.di

import com.example.spyfall.data.repository.GameRepositoryImpl
import com.example.spyfall.data.repository.UserRepositoryImpl
import com.example.spyfall.domain.repository.GameRepository
import com.example.spyfall.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BindModule {

    @Binds
    fun bindUserRepositoryImplToUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    fun bindGameRepositoryImplToGameRepository(
        gameRepositoryImpl: GameRepositoryImpl
    ): GameRepository
}
