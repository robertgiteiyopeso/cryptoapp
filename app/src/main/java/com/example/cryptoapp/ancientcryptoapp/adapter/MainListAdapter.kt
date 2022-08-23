package com.example.cryptoapp.ancientcryptoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.RecyclerMainItemBinding
import com.example.cryptoapp.ancientcryptoapp.domain.CryptoCoinModel

class MainListAdapter(private val onCardClicked: (model: CryptoCoinModel) -> Unit) :
    RecyclerView.Adapter<MainListAdapter.MainListViewHolder>() {

    var list = listOf<CryptoCoinModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MainListViewHolder(
        private val binding: RecyclerMainItemBinding,
        val onCardClicked: (model: CryptoCoinModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: CryptoCoinModel, position: Int) {
            binding.tvCoin.text = model.toString()

            //Set alternating background color
            if (position % 2 == 0)
                binding.tvCoin.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.gray1
                    )
                )
            else
                binding.tvCoin.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.gray2
                    )
                )

            //Set click listeners
            binding.tvCoin.setOnClickListener {
                onCardClicked(list[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        val view =
            RecyclerMainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainListViewHolder(view, onCardClicked)
    }

    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount() = list.size
}