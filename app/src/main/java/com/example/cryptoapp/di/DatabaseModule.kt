package com.example.cryptoapp.di

import android.content.Context
import androidx.room.Room
import com.example.cryptoapp.repository.database.MovieDao
import com.example.cryptoapp.repository.database.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideMovieDao(database: MovieDatabase) : MovieDao =
        database.getMovieDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context) : MovieDatabase =
        Room.databaseBuilder(
            appContext,
            MovieDatabase::class.java,
            "movie_database"
        ).build()
}