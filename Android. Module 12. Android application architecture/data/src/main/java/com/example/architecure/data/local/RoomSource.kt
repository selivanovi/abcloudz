package com.example.architecure.data.local

import com.example.architecure.data.LocalDataSource
import com.example.architecure.data.local.dao.MovieDao
import com.example.architecure.data.local.entity.Movie
import kotlinx.coroutines.flow.Flow

class RoomSource(
    private val movieDao: MovieDao,
) : LocalDataSource {

    override fun getMovies(): Flow<List<Movie>> =
        movieDao.getMovies()


    override fun getMovieById(movieId: Int): Flow<Movie> =
        movieDao.getMovie(movieId)

    override suspend fun refreshMovies(movies: List<Movie>) =
        movieDao.refreshMovies(movies)
}