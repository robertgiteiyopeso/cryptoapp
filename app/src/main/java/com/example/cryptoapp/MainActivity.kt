package com.example.cryptoapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cryptoList = getCryptoCoins(this)
        for(i in cryptoList)
            Log.i(TAG, i.toString())
        Log.i(TAG, cryptoList[0].javaClass.toString())

    }

    fun getCryptoCoins(context: Context): List<CryptoCoin> {
        lateinit var jsonString: String
        try {
            jsonString = context.resources.openRawResource(R.raw.input)
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            Log.e(TAG, "oops n-am reusit sa citim bine")
            return emptyList()
        }

        val cryptoList = object : TypeToken<List<CryptoCoin>>() {}.type
        return Gson().fromJson(jsonString, cryptoList)
    }
}