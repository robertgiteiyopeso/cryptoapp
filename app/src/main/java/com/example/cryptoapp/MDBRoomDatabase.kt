package com.example.cryptoapp

import android.content.Context
import androidx.room.Room

object MDBRoomDatabase {

    const val MOVIE_DATABASE_TAG = "movie_database"
    var database: MovieDatabase? = null

    fun getInstance(context: Context): MovieDatabase? {
        if (database == null) {
            database = Room.databaseBuilder(
                context, MovieDatabase::class.java,
                MOVIE_DATABASE_TAG
            ).build()
        }

        return database
    }

}