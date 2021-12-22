package com.example.architecture.app

import android.content.Context
import com.example.architecture.domain.MovieCacheController
import com.example.architecture.domain.usecase.GetLocalMovieByIdUseCase
import com.example.architecture.domain.usecase.GetLocalMoviesUseCase
import com.example.architecture.screen.MovieDetailsFragment

import com.example.architecture.screen.MovieListFragment
import com.example.architecture.viewmodel.DetailsMovieViewModel
import com.example.architecture.viewmodel.MovieListViewModel

import dagger.*
import javax.inject.Singleton


@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    fun inject(fragment: MovieListFragment)
    fun inject(fragment: MovieDetailsFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}







