package com.example.cryptoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.databinding.ActorCardBinding
import com.example.cryptoapp.domain.ActorModel

class ActorAdapter : RecyclerView.Adapter<ActorAdapter.ActorViewHolder>() {

    var list = listOf<ActorModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ActorViewHolder(private val binding: ActorCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ActorModel) {
            //Set up profile photo
            val endpoint = "https://image.tmdb.org/t/p/w500"
            Glide.with(binding.root.context).load(endpoint + model.profilePath).circleCrop()
                .into(binding.ivProfilePhoto)

            //Set up name
            binding.tvName.text = model.name
            binding.tvName.isSelected = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val view = ActorCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}