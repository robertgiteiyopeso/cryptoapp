package com.example.cryptoapp.ancientcryptoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.R
import com.example.cryptoapp.ancientcryptoapp.adapter.MainListAdapter
import com.example.cryptoapp.databinding.ActivityMainBinding

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

        //Set up RecyclerView
        binding.rvCoinList.layoutManager = LinearLayoutManager(this)
        val adapter = MainListAdapter {
            val intent = Intent(this, CoinDetailsActivity::class.java)
            intent.putExtra("id_coin", it.id)
            startActivity(intent)
        }
        adapter.list = cryptoList.sortedBy { it.rank }
        binding.rvCoinList.adapter = adapter

    }
}