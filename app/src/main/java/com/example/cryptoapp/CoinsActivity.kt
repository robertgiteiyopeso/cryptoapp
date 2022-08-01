package com.example.cryptoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.adapter.MainListAdapter
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

        //Set up RecyclerView
        binding.rvCoinList.layoutManager = LinearLayoutManager(this)
        val adapter = MainListAdapter()
        adapter.list = cryptoList
        binding.rvCoinList.adapter = adapter

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