package com.example.architecture.app

import com.example.architecture.domain.MovieCacheController
import com.example.architecture.domain.usecase.GetLocalMovieByIdUseCase
import com.example.architecture.domain.usecase.GetLocalMoviesUseCase
import com.example.architecture.viewmodel.DetailsMovieViewModel
import com.example.architecture.viewmodel.MovieListViewModel
import dagger.Module
import dagger.Provides

@Module(includes = [AppBindModule::class, DataModule::class, DomainModule::class])
class AppModule {

    @Provides
    fun provideMovieListViewModel(
        getLocalMoviesUseCase: GetLocalMoviesUseCase,
        cacheController: MovieCacheController
    ): MovieListViewModel {
        return MovieListViewModel(getLocalMoviesUseCase, cacheController)
    }

    @Provides
    fun provideDetailsMovieViewModel(
        getLocalMovieByIdUseCase: GetLocalMovieByIdUseCase
    ): DetailsMovieViewModel {
        return DetailsMovieViewModel(getLocalMovieByIdUseCase)
    }
}