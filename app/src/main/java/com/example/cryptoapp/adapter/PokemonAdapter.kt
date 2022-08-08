package com.example.cryptoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.PokemonsQuery
import com.example.cryptoapp.databinding.PokemonCardBinding

class PokemonAdapter : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    var list = listOf<PokemonsQuery.Pokemon?>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class PokemonViewHolder(private val binding: PokemonCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: PokemonsQuery.Pokemon?) {
            //Set up background
            Glide.with(binding.root.context).load(model?.image)
                .into(binding.ivPokemonImage)

            //Set up name
            binding.tvName.text = model?.name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = PokemonCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}