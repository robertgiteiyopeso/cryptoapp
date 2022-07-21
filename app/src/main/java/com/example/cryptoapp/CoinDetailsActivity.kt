package com.example.cryptoapp

import android.graphics.Color
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
        val fileId = resources.getIdentifier(fileName, "raw", packageName)
        //Get the coin's detalis
        val details = FileUtils.getCryptoCoinDetails(this, fileId)

        //Display the data on the screen
        val headerTitle = "${details.rank}. ${details.name} (${details.symbol})"
        binding.tvTitleHeader.text = headerTitle

        if(details.isActive){
            binding.tvStatus.let {
                it.text = getString(R.string.active)
                it.setTextColor(Color.GREEN)
            }
        }
        else{
            binding.tvStatus.let {
                it.text = getString(R.string.inactive)
                it.setTextColor(Color.RED)
            }
        }

        binding.tvDescription.text = details.description
    }
}