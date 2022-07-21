package com.example.cryptoapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoapp.databinding.ActivityCoinDetailsBinding

class CoinDetailsActivity : AppCompatActivity() {

    private val  TAG = "CoinDetailsActivity"
    private lateinit var binding: ActivityCoinDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get the id of the coin we need to display
        val idCoin = intent.getStringExtra("id_coin")
        val fileName = idCoin?.replace("-", "_")
        val file = resources.getIdentifier(fileName, "raw", packageName)
        //Get the coin's detalis
        val details = FileUtils.getCryptoCoins(this, file)
        Log.i(TAG, details.toString())
    }
}