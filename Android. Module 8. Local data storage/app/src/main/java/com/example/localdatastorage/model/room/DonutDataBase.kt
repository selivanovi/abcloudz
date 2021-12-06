package com.example.localdatastorage.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.localdatastorage.model.room.dao.DonutsDao
import com.example.localdatastorage.model.room.entities.*
import com.example.localdatastorage.utils.DonutJsonParser
import com.example.localdatastorage.utils.toDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Database(
    entities = [
        Donut::class,
        Topping::class,
        Batter::class,
        DonutBatterCrossRef::class,
        DonutToppingCrossRef::class
    ],
    version = 2
)
abstract class DonutDataBase : RoomDatabase() {

    abstract val donutsDao: DonutsDao

    companion object {
        @Volatile
        private var INSTANCE: DonutDataBase? = null

        fun getInstance(context: Context): DonutDataBase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DonutDataBase::class.java,
                    "database-donut"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        val dataBase = getInstance(context)
                        val donuts = DonutJsonParser.pareJson(context, "JSON 2.txt").map {
                            it.toDTO()
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            dataBase.donutsDao.insertDonuts(donuts)
                        }
                    }
                }).addMigrations(MIGRATION_1_2)
                    .build().also {
                        INSTANCE = it
                    }
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE donut ADD allergy TEXT")
            }
        }

    }
}