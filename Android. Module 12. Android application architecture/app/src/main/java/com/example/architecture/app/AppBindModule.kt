package com.example.architecture.app

import com.example.architecture.domain.repostitory.MovieRepository
import com.example.architecure.data.LocalDataSource
import com.example.architecure.data.RemoteDataSource
import com.example.architecure.data.local.RoomSource
import com.example.architecure.data.remote.ApiClient
import com.example.architecure.data.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface AppBindModule {

    @Binds
    fun bindMovieRepositoryImplToMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    fun bindMovieRoomSourceToLocalDataSource(
        roomSource: RoomSource
    ): LocalDataSource

    @Binds
    fun bindMovieApiClientToRemoteDataSource(
        apiClient: ApiClient
    ): RemoteDataSource
}