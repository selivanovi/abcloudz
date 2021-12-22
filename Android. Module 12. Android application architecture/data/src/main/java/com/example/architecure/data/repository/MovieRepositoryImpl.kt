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

    override suspend fun getRemoteMovies(): List<MovieDomain>? =
        remoteDataSource.getMovies()?.map{ it.toDomain() }


    override suspend fun getRemoteMovieById(movieId: String): MovieDomain? =
        remoteDataSource.getMovieById(movieId)?.toDomain()

    override fun getLocalMovies(): Flow<List<MovieDomain>> =
        localDataSource.getMovies().map { list -> list.map { it.toDomain() } }

    override fun getLocalMovieById(movieId: Long): Flow<MovieDomain> =
        localDataSource.getMovieById(movieId).map { it.toDomain() }

    override suspend fun refreshMovies(movies: List<MovieDomain>) =
        localDataSource.refreshMovies(movies.map { it.toData() })
}