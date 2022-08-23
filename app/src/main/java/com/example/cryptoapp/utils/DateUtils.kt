package com.example.cryptoapp.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate

object DateUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun parse(date: String): LocalDate =
        try {
            LocalDate.parse(date)
        } catch (e: Exception) {
            Log.e("DateUtils: ", e.message.toString())
            LocalDate.now()
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun now(): LocalDate = LocalDate.now()
}