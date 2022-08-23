package com.example.cryptoapp.ancientcryptoapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.cryptoapp.databinding.GridviewTagItemBinding
import com.example.cryptoapp.ancientcryptoapp.domain.GridItemTagModel

class GridAdapter(context: Context, private val items: List<GridItemTagModel>) :
    ArrayAdapter<GridItemTagModel>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = GridviewTagItemBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.tvGridItem.text = items[position].text
        return binding.root
    }
}