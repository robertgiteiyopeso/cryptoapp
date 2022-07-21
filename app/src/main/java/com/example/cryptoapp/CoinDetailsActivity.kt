package com.example.cryptoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cryptoapp.databinding.ActivityCoinDetailsBinding

class CoinDetailsActivity : AppCompatActivity() {

    private val  TAG = "CoinDetailsActivity"
    private lateinit var binding: ActivityCoinDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val message = intent.getStringExtra("id_coin")
        Log.i(TAG, message.toString())
    }
}