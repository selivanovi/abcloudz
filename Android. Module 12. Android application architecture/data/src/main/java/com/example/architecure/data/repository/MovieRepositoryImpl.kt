package com.example.architecure.data.repository

import com.example.architecture.domain.entity.MovieDomain
import com.example.architecture.domain.repostitory.MovieRepository
import com.example.architecure.data.LocalDataSource
import com.example.architecure.data.RemoteDataSource
import com.example.architecure.data.remote.Resource
import com.example.architecure.data.toData
import com.example.architecure.data.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : MovieRepository {

    override suspend fun getRemoteMovies(): Result<List<MovieDomain>?> =
        when (val resource = remoteDataSource.getMovies()) {
            is Resource.Success -> {
                val mappedList = resource.data?.map { it.toDomain() }
                Result.success(mappedList)
            }
            is Resource.Error -> Result.failure(resource.throwable!!)
        }


    override suspend fun getRemoteMovieById(movieId: String): Result<MovieDomain?> =
        when (val resource = remoteDataSource.getMovieById(movieId)) {
            is Resource.Success -> {
                val data = resource.data?.toDomain()
                Result.success(data)
            }
            is Resource.Error -> Result.failure(resource.throwable!!)
        }

    override suspend fun getLocalMovies(): Flow<List<MovieDomain>> =
        localDataSource.getMovies().map { list -> list.map { it.toDomain() } }

    override suspend fun getLocalMovieById(movieId: Int): Flow<MovieDomain> =
        localDataSource.getMovieById(movieId).map { it.toDomain() }

    override suspend fun refreshMovies(movies: List<MovieDomain>) =
        localDataSource.refreshMovies(movies.map { it.toData() })
}