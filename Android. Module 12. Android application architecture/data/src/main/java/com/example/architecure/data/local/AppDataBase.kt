package com.example.architecure.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.architecure.data.local.dao.MovieDao
import com.example.architecure.data.local.entity.Movie


@Database(
    entities = [
        Movie::class
    ],
    version = 1,
)
abstract class AppDataBase : RoomDatabase() {

    abstract val movieDao: MovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "database-character"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
