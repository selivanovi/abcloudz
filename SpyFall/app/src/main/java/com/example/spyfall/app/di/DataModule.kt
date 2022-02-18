package com.example.spyfall.app.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase =
        FirebaseDatabase.getInstance()

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
}
