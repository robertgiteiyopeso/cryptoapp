package com.example.cryptoapp

import android.util.Log
import java.time.LocalDate

object DateUtils {
    fun parse(date: String): LocalDate =
        try {
            LocalDate.parse(date)
        } catch (e: Exception) {
            Log.e("DateUtils: ", e.message.toString())
            LocalDate.now()
        }

    fun now(): LocalDate = LocalDate.now()
}