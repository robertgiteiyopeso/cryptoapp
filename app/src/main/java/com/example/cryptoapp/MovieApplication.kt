package com.example.cryptoapp

import android.app.Application
import android.content.SharedPreferences

class MovieApplication : Application() {

    val sharedPrefSession: SharedPreferences by lazy {
        this.getSharedPreferences(
            "session_id",
            MODE_PRIVATE
        )
    }

    val sharedPrefHistory: SharedPreferences by lazy {
        this.getSharedPreferences(
            "search_history",
            MODE_PRIVATE
        )
    }

    val dao: MovieDao by lazy {
        MDBRoomDatabase.getInstance(this).getMovieDao()
    }

    val appContainer = AppContainer()
}