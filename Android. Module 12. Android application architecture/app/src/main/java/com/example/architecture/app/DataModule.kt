package com.example.architecture.app

import com.example.architecure.data.local.RoomSource
import com.example.architecure.data.remote.ApiClient
import com.example.architecure.data.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class, StorageModule::class])
class DataModule {
    @Provides
    fun provideMovieRepositoryImpl(
        apiClient: ApiClient,
        roomSource: RoomSource
    ): MovieRepositoryImpl {
        return MovieRepositoryImpl(apiClient, roomSource)
    }
}