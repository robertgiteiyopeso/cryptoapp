package com.example.cryptoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.example.cryptoapp.domain.CryptoCoinModel

const val TAG = "CoinsActivity"
const val errmsg = "oops n-am reusit sa citim bine"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get a list of CryptoCoins from input.json
        val fileId = R.raw.input
        val cryptoList = FileUtils.getCryptoCoins(this, fileId)
        val tvList = listOf(binding.tv1, binding.tv2, binding.tv3)

        //Load the CryptoCoins into the TextViews
        setViews(tvList, cryptoList)

        //Set click listeners for TextViews
        setListeners(tvList, cryptoList)

    }

    private fun setListeners(views: List<TextView>, content: List<CryptoCoinModel>) {
        for(i in views.indices) {
            views[i].setOnClickListener {
                val intent = Intent(this, CoinDetailsActivity::class.java)
                intent.putExtra("id_coin", content[i].id)
                startActivity(intent)
            }
        }
    }

    private fun setViews(views: List<TextView>, content: List<CryptoCoinModel>) {
        if(views.isNotEmpty())
            for(i in views.indices) {
                views[i].text = content[i].toString()
            }
        else {
            for(i in views.indices)
            views[i].text = getString(R.string.error_text)
        }
    }

}