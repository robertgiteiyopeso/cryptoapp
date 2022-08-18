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

class MovieAdapter(
    private val onMovieCardHold: (model: MovieModel) -> Unit,
    private val onMovieCardClick: (movieId: Int) -> Unit
) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    companion object {
        const val endpoint = "https://image.tmdb.org/t/p/w500"
    }

    var list = listOf<MovieModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun modifyOneElement(model: MovieModel) {
        val position = list.indexOf(model)
        list = list.map {
            if (model.id == it.id) {
                return@map it.copy(isFavorite = !it.isFavorite)
            } else
                it
        }
        notifyItemChanged(position)
    }

    inner class MovieViewHolder(
        private val binding: MovieCardBinding,
        private val onMovieCardHold: (model: MovieModel) -> Unit,
        private val onMovieCardClick: (movieId: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MovieModel) {
            //Set up background
            Glide.with(binding.root.context).load(endpoint + model.posterPath)
                .into(binding.ivMoviePoster)

            //Set up Must Watch
            binding.tvMustWatch.visibility =
                if (model.isTwoMonthsOlder()) View.VISIBLE else View.GONE

            //Border and <3 if favorite
            setFavorite(model.isFavorite)

            //Set up hold listener
            binding.cvCard.setOnLongClickListener {
                onMovieCardHold(model)
                true
            }

            //Set up click listener
            binding.cvCard.setOnClickListener {
                onMovieCardClick(model.id)
            }
        }

        private fun setFavorite(isFavorite: Boolean) {
            if (isFavorite) {
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
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = MovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(view, onMovieCardHold, onMovieCardClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}