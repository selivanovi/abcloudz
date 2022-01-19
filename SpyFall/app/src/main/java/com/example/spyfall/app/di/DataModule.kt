package com.example.spyfall.app.di

import android.content.Context
import android.content.SharedPreferences
import com.example.spyfall.data.local.SharedPreferencesStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

//    @Provides
//    @Singleton
//    fun provideSharedPreferencesStorage(sharedPreferences: SharedPreferences): SharedPreferencesStorage =
//        SharedPreferencesStorage(sharedPreferences)

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(null, Context.MODE_PRIVATE)
}