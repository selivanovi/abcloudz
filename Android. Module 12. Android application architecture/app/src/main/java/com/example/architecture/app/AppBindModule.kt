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

    @Suppress("FunctionName")
    @Binds
    fun bindMovieRepositoryImpl_to_MovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Suppress("FunctionName")
    @Binds
    fun bindMovieRoomSource_to_LocalDataSource(
        roomSource: RoomSource
    ): LocalDataSource

    @Suppress("FunctionName")
    @Binds
    fun bindMovieApiClient_to_RemoteDataSource(
        apiClient: ApiClient
    ): RemoteDataSource
}