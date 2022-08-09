package com.example.cryptoapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieDatabaseModel::class],
    version = 1,
    exportSchema = false
)

abstract class MovieDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

}