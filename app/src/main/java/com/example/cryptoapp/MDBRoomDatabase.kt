package com.example.cryptoapp

import android.content.Context
import androidx.room.Room

object MDBRoomDatabase {

    private const val MOVIE_DATABASE_TAG = "movie_database"
    private lateinit var database: MovieDatabase

    fun getInstance(context: Context): MovieDatabase {
        if (!this::database.isInitialized) {
            database = Room.databaseBuilder(
                context, MovieDatabase::class.java,
                MOVIE_DATABASE_TAG
            ).build()
        }

        return database
    }

}