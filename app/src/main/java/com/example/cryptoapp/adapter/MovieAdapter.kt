package com.example.cryptoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.databinding.MovieCardBinding
import com.example.cryptoapp.domain.ActorModel
import com.example.cryptoapp.domain.MovieModel

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    var list = listOf<MovieModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MovieViewHolder(private val binding: MovieCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MovieModel) {
            //Set up background
            val endpoint = "https://image.tmdb.org/t/p/w500"
            Glide.with(binding.root.context).load(endpoint + model.posterPath)
                .into(binding.ivMoviePoster)

            //Set up Must Watch
            if (model.voteAverage > 8.5)
                binding.tvMustWatch.visibility = View.VISIBLE
            else
                binding.tvMustWatch.visibility = View.INVISIBLE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = MovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}