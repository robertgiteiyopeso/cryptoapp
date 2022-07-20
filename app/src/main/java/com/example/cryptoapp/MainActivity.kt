package com.example.cryptoapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.Exception

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get a list of CryptoCoins from input.json
        val cryptoList = getCryptoCoins(this)

        //Load the CryptoCoins into the TextViews
        if (cryptoList.isNotEmpty()) {
            binding.tv1.setText(cryptoList[0].toString())
            binding.tv2.setText(cryptoList[1].toString())
            binding.tv3.setText(cryptoList[2].toString())
        } else {
            binding.tv1.setText(getString(R.string.error_text))
            binding.tv2.setText(getString(R.string.error_text))
            binding.tv3.setText(getString(R.string.error_text))
        }

    }

    fun getCryptoCoins(context: Context): List<CryptoCoin> {
        lateinit var jsonString: String
        try {
            jsonString = context.resources.openRawResource(R.raw.input)
                .bufferedReader()
                .use { it.readText() }
        } catch (exception: Exception) {
            Log.e(TAG, "oops n-am reusit sa citim bine")
            return emptyList()
        }

        val cryptoListType = object : TypeToken<List<CryptoCoin>>() {}.type
        return Gson().fromJson(jsonString, cryptoListType)
    }
}