package com.example.asyncoperations.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asyncoperations.model.room.dao.CharacterDao
import com.example.asyncoperations.model.room.entities.Episode
import com.example.asyncoperations.model.room.entities.Character

@Database(
    entities = [
        Character::class,
        Episode::class,
    ],
    version = 1,
)
abstract class CharacterDataBase : RoomDatabase() {

    abstract val characterDao: CharacterDao

    companion object {
        @Volatile
        private var INSTANCE: CharacterDataBase? = null

        fun getInstance(context: Context): CharacterDataBase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CharacterDataBase::class.java,
                    "database-character"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}