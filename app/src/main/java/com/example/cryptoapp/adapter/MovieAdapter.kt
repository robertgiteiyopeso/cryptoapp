package com.example.cryptoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.MovieCardBinding
import com.example.cryptoapp.domain.MovieModel

class MovieAdapter(private val onMovieCardHold: (model: MovieModel) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    var list = listOf<MovieModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MovieViewHolder(
        private val binding: MovieCardBinding,
        private val onMovieCardHold: (model: MovieModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MovieModel) {
            //Set up background
            val endpoint = "https://image.tmdb.org/t/p/w500"
            Glide.with(binding.root.context).load(endpoint + model.posterPath)
                .into(binding.ivMoviePoster)

            //Set up Must Watch
            if (model.isTwoMonthsOlder()) {
                binding.tvMustWatch.visibility = View.VISIBLE
            } else {
                binding.tvMustWatch.visibility = View.GONE
            }

            //Set up border
            if (model.isFavorite) {
                //<3
                binding.vHeart.visibility = View.VISIBLE
                binding.vHeartBorder.visibility = View.VISIBLE

                //Border
                binding.cvCard.strokeColor =
                    ContextCompat.getColor(binding.root.context, R.color.secondaryColor)
            } else {
                //</3
                binding.vHeart.visibility = View.GONE
                binding.vHeartBorder.visibility = View.GONE

                //Border
                binding.cvCard.strokeColor =
                    ContextCompat.getColor(binding.root.context, R.color.transparent)
            }

            //Set up hold listener
            binding.cvCard.setOnLongClickListener {
                if (model.isFavorite) {
                    onMovieCardHold(model)
                    model.isFavorite = false

                    //</3
                    binding.vHeart.visibility = View.GONE
                    binding.vHeartBorder.visibility = View.GONE

                    //Border
                    binding.cvCard.strokeColor =
                        ContextCompat.getColor(binding.root.context, R.color.transparent)
                } else {
                    onMovieCardHold(model)
                    model.isFavorite = true

                    //<3
                    binding.vHeart.visibility = View.VISIBLE
                    binding.vHeartBorder.visibility = View.VISIBLE

                    //Border
                    binding.cvCard.strokeColor =
                        ContextCompat.getColor(binding.root.context, R.color.secondaryColor)
                }
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = MovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(view, onMovieCardHold)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}