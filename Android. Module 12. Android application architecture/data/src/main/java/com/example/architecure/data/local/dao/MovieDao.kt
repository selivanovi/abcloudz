package com.example.architecure.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.example.architecure.data.local.entity.Movie
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

@Dao
interface MovieDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM movie")
    fun getMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    fun getMovie(movieId: Int): Flow<Movie>

    @Query("DELETE FROM movie")
    suspend fun deleteMovies()

    @Transaction
    suspend fun refreshMovies(movies: List<Movie>) {
        deleteMovies()
        insertMovies(movies)
    }

}