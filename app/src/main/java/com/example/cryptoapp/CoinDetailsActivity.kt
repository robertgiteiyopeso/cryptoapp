package com.example.cryptoapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoapp.databinding.ActivityCoinDetailsBinding
import com.example.cryptoapp.domain.CryptoCoinDetailsModel
import com.example.cryptoapp.domain.GridItemTagModel

class CoinDetailsActivity : AppCompatActivity() {

    private val TAG = "CoinDetailsActivity"
    private lateinit var binding: ActivityCoinDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get the coin's details
        val details = getCoinDetails()

        //Display the data on the screen
        displayData(binding, details)
    }

    private fun getCoinDetails() : CryptoCoinDetailsModel {
        val idCoin = intent.getStringExtra("id_coin")
        val fileName = idCoin?.replace("-", "_")
        val fileId = resources.getIdentifier(fileName, "raw", packageName)
        return FileUtils.getCryptoCoinDetails(this, fileId)
    }

    private fun displayData(binding: ActivityCoinDetailsBinding, details: CryptoCoinDetailsModel) {
        val headerTitle = "${details.rank}. ${details.name} (${details.symbol})"
        binding.tvTitleHeader.text = headerTitle

        if (details.isActive) {
            binding.tvStatus.let {
                it.text = getString(R.string.active)
                it.setTextColor(Color.GREEN)
            }
        } else {
            binding.tvStatus.let {
                it.text = getString(R.string.inactive)
                it.setTextColor(Color.RED)
            }
        }

        binding.tvDescription.text = details.description

        val tagList = details.tags.map { it -> GridItemTagModel(it.name) }
        binding.grdTags.adapter = GridAdapter(this, tagList)

        binding.tvTeamLeader1.text = details.team[0].name
        binding.tvTeamRole1.text = details.team[0].position
        binding.tvTeamLeader2.text = details.team[1].name
        binding.tvTeamRole2.text = details.team[1].position
    }
}