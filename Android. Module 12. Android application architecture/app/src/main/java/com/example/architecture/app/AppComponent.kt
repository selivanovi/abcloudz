package com.example.architecture.app

import android.content.Context
import com.example.architecture.screen.MovieDetailsFragment

import com.example.architecture.screen.MovieListFragment
import dagger.BindsInstance
import dagger.Component

import javax.inject.Singleton


@Component(
    modules = [
        NetworkModule::class,
        StorageModule::class,
        AppBindModule::class,
    ]
)
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







