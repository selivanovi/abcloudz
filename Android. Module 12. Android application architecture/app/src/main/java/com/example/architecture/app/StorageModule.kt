package com.example.architecture.app

import android.content.Context
import androidx.room.Room
import com.example.architecure.data.local.AppDataBase
import com.example.architecure.data.local.RoomSource
import com.example.architecure.data.local.dao.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Provides
    fun provideRoomSource(dao: MovieDao): RoomSource {
        return RoomSource(dao)
    }

    @Singleton
    @Provides
    fun provideMovieDao(dataBase: AppDataBase): MovieDao {
        return dataBase.movieDao
    }

    @Singleton
    @Provides
    fun provideAppDataBase(context: Context): AppDataBase {
        return Room.databaseBuilder(context, AppDataBase::class.java, "database-movie").build()
    }
}