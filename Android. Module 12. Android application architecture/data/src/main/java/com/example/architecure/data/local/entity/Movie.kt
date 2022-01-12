package com.example.architecure.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String?,
    val originalLanguage: String?,
    val releaseDate: String?,
    val posterPath: String?
)
