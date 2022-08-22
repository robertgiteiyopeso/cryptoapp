package com.example.cryptoapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class MovieApplication : Application() {

    val sharedPref: SharedPreferences by lazy {
        this.getSharedPreferences(
            "session_id",
            Context.MODE_PRIVATE
        )
    }

    val dao: MovieDao by lazy {
        MDBRoomDatabase.getInstance(this).getMovieDao()
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}