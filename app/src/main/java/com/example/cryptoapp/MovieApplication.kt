package com.example.cryptoapp

import android.app.Application
import android.content.SharedPreferences

class MovieApplication : Application() {

    private val sharedPrefSession: SharedPreferences by lazy {
        this.getSharedPreferences(
            "session_id",
            MODE_PRIVATE
        )
    }

    private val sharedPrefHistory: SharedPreferences by lazy {
        this.getSharedPreferences(
            "search_history",
            MODE_PRIVATE
        )
    }

    private val movieDao: MovieDao by lazy {
        MDBRoomDatabase.getInstance(this).getMovieDao()
    }

    val mdbRepo: MDBRepositoryRetrofit by lazy {
        MDBRepositoryRetrofit(sharedPrefSession, sharedPrefHistory, movieDao)
    }
}