package com.example.cryptoapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception

const val TAG = "MainActivity"
const val errmsg = "oops n-am reusit sa citim bine"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get a list of CryptoCoins from input.json
        val cryptoList = Logic().getCryptoCoins(this)

        //Load the CryptoCoins into the TextViews
        if (cryptoList.isNotEmpty()) {
            binding.tv1.text = cryptoList[0].toString()
            binding.tv2.text = cryptoList[1].toString()
            binding.tv3.text = cryptoList[2].toString()
        } else {
            binding.tv1.text = getString(R.string.error_text)
            binding.tv2.text = getString(R.string.error_text)
            binding.tv3.text = getString(R.string.error_text)
        }

    }


}