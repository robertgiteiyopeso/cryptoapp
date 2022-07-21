package com.example.cryptoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.example.cryptoapp.domain.CryptoCoinDetailsModel

const val TAG = "CoinsActivity"
const val errmsg = "oops n-am reusit sa citim bine"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get a list of CryptoCoins from input.json
        val cryptoList = FileUtils.getCryptoCoins(this)

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

        //Set click listeners for TextViews
        binding.tv1.setOnClickListener {
            val intent = Intent(this, CoinDetailsActivity::class.java)
            intent.putExtra("id_coin", cryptoList[0].id)
            startActivity(intent)
        }

        binding.tv2.setOnClickListener {
            val intent = Intent(this, CoinDetailsActivity::class.java)
            intent.putExtra("id_coin", cryptoList[1].id)
            startActivity(intent)
        }

        binding.tv3.setOnClickListener {
            val intent = Intent(this, CoinDetailsActivity::class.java)
            intent.putExtra("id_coin", cryptoList[2].id)
            startActivity(intent)
        }

    }


}