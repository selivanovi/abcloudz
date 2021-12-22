package com.example.architecture.app

import com.example.architecture.domain.MovieCacheController
import com.example.architecture.domain.usecase.*
import com.example.architecure.data.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideGetLocalMoviesUseCase(movieRepository: MovieRepositoryImpl): GetLocalMoviesUseCase {
        return GetLocalMoviesUseCase(movieRepository)
    }

    @Provides
    fun provideGetLocalMovieByIdUseCase(movieRepository: MovieRepositoryImpl): GetLocalMovieByIdUseCase {
        return GetLocalMovieByIdUseCase(movieRepository)
    }

    @Provides
    fun provideGetRemoteMoviesUseCase(movieRepository: MovieRepositoryImpl): GetRemoteMoviesUseCase {
        return GetRemoteMoviesUseCase(movieRepository)
    }

    @Provides
    fun provideGetRemoteMovieByIdUseCase(movieRepository: MovieRepositoryImpl): GetRemoteMovieByIdUseCase {
        return GetRemoteMovieByIdUseCase(movieRepository)
    }

    @Provides
    fun provideRefreshLocalUseCase(movieRepository: MovieRepositoryImpl): RefreshLocalUseCase {
        return RefreshLocalUseCase(movieRepository)
    }

    @Provides
    fun provideCacheController(
        getRemoteMoviesUseCase: GetRemoteMoviesUseCase,
        refreshLocalUseCase: RefreshLocalUseCase
    ) : MovieCacheController {
        return MovieCacheController(getRemoteMoviesUseCase, refreshLocalUseCase)
    }
}
