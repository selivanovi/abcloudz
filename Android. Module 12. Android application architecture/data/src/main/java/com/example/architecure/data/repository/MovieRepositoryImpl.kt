package com.example.architecure.data.repository

import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.domain.repostitory.MovieRepository
import com.example.architecure.data.LocalDataSource
import com.example.architecure.data.RemoteDataSource
import com.example.architecure.data.toData
import com.example.architecure.data.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : MovieRepository {

    override fun observeMovies(): Flow<List<MovieDomain>> =
        localDataSource.getMovies().map { list -> list.map { it.toDomain() } }

    override fun observeMovieById(movieId: Long): Flow<MovieDomain> =
        localDataSource.getMovieById(movieId).map { it.toDomain() }

    override suspend fun refreshMovies() {
        val result = remoteDataSource.getMovies()
        result?.let { listNotNull ->
            localDataSource.refreshMovies(
                listNotNull.map { it.toData() }
            )
        }
    }
}