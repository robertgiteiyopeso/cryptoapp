package com.example.android.hilt.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class SharedPreferencesHistory

@Qualifier
annotation class SharedPreferencesSession

@InstallIn(SingletonComponent::class)
@Module
class SharedPreferencesModule {

    @SharedPreferencesHistory
    @Provides
    @Singleton
    fun provideSharedPrefHistory(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences(
            "search_history",
            Application.MODE_PRIVATE
        )

    @SharedPreferencesSession
    @Provides
    @Singleton
    fun provideSharedPrefSession(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences(
            "session_id",
            Application.MODE_PRIVATE
        )
}