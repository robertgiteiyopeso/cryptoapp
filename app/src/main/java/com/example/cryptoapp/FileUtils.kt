package com.example.cryptoapp

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception

class FileUtils {

    fun getCryptoCoins(context: Context): List<CryptoCoinModel> {
        lateinit var jsonString: String
        try {
            jsonString = context.resources.openRawResource(R.raw.input)
                .bufferedReader()
                .use { it.readText() }
        } catch (exception: Exception) {
            Log.e(TAG, errmsg)
            return emptyList()
        }

        val cryptoListType = object : TypeToken<List<CryptoCoinModel>>() {}.type
        return Gson().fromJson(jsonString, cryptoListType)
    }
}