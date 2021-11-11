package com.example.localdatastorage.model.room

import com.example.localdatastorage.model.room.dao.DonutsDao

class RoomLocalDataSource(
    private val dao: DonutsDao
) : LocalDataSource {
}