package com.example.cryptoapp

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoapp.adapter.GridAdapter
import com.example.cryptoapp.adapter.ListAdapter
import com.example.cryptoapp.databinding.ActivityCoinDetailsBinding
import com.example.cryptoapp.domain.CryptoCoinDetailsModel
import com.example.cryptoapp.domain.GridItemTagModel
import com.example.cryptoapp.domain.ListItemTeamMemberModel

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

    private fun getCoinDetails(): CryptoCoinDetailsModel {
        val idCoin = intent.getStringExtra("id_coin")
        val fileName = idCoin?.replace("-", "_")
        val fileId = resources.getIdentifier(fileName, "raw", packageName)
        return FileUtils.getCryptoCoinDetails(this, fileId)
    }

    private fun displayData(binding: ActivityCoinDetailsBinding, details: CryptoCoinDetailsModel) {
        // Header
        val headerTitle = "${details.rank}. ${details.name} (${details.symbol})"
        binding.tvTitleHeader.text = headerTitle

        // Status
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

        // Description
        binding.tvDescription.text = details.description

        // Tags
        val tagList = details.tags.map { GridItemTagModel(it.name) }
        binding.grdTags.adapter = GridAdapter(this, tagList)

        // Team Members
        val teamMembers = details.team.map { ListItemTeamMemberModel(it.name, it.position) }
        binding.lvTeamMembers.adapter = ListAdapter(this, teamMembers)
    }
}