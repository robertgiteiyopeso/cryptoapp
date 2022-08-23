package com.example.cryptoapp.ancientcryptoapp

import android.content.Context
import android.util.Log
import com.example.cryptoapp.ancientcryptoapp.domain.CryptoCoinDetailsModel
import com.example.cryptoapp.ancientcryptoapp.domain.CryptoCoinModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception

class FileUtils {

    companion object {
        fun getCryptoCoins(context: Context, fileId: Int): List<CryptoCoinModel> {
            lateinit var jsonString: String
            try {
                jsonString = context.resources.openRawResource(fileId)
                    .bufferedReader()
                    .use { it.readText() }
            } catch (exception: Exception) {
                Log.e(TAG, errmsg)
                return emptyList()
            }

            val cryptoListType = object : TypeToken<List<CryptoCoinModel>>() {}.type
            return Gson().fromJson(jsonString, cryptoListType)
        }

        fun getCryptoCoinDetails(context: Context, fileId: Int): CryptoCoinDetailsModel {
            lateinit var jsonString: String
            try {
                jsonString = context.resources.openRawResource(fileId)
                    .bufferedReader()
                    .use { it.readText() }
            } catch (exception: Exception) {
                Log.e(TAG, errmsg)
                return CryptoCoinDetailsModel()
            }

            val cryptoListType = object : TypeToken<CryptoCoinDetailsModel>() {}.type
            return Gson().fromJson(jsonString, cryptoListType)
        }
    }
}