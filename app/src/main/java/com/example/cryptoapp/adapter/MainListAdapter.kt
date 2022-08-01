package com.example.cryptoapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.RecyclerMainItemBinding
import com.example.cryptoapp.domain.CryptoCoinModel

class MainListAdapter() : RecyclerView.Adapter<MainListAdapter.MainListViewHolder>() {

    var list = listOf<CryptoCoinModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MainListViewHolder(private val binding: RecyclerMainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(model: CryptoCoinModel, position: Int) {
                binding.tvCoin.text = model.toString()
                if(position % 2 == 1)
                    binding.tvCoin.setBackgroundColor(Color.RED)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        val view = RecyclerMainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount() = list.size
}