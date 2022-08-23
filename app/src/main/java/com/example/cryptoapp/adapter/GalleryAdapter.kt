package com.example.cryptoapp.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.cryptoapp.domain.GalleryModel
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.utils.DateUtils
import com.example.cryptoapp.databinding.GalleryImageBinding

class GalleryAdapter(private val onMovieCardClick: (movieId: Int) -> Unit) :
    ListAdapter<GalleryModel, GalleryAdapter.GalleryViewHolder>(object :
        DiffUtil.ItemCallback<GalleryModel>() {
        override fun areItemsTheSame(
            oldItem: GalleryModel,
            newItem: GalleryModel
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: GalleryModel,
            newItem: GalleryModel
        ) = oldItem == newItem
    }) {

    companion object {
        const val endpoint = "https://image.tmdb.org/t/p/w500"
    }

    inner class GalleryViewHolder(
        private val binding: GalleryImageBinding,
        private val onMovieCardClick: (movieId: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: GalleryModel) {
            //Load image
            Glide.with(binding.root.context).load(endpoint + model.imageUrl)
                .into(binding.ivGalleryImage)

            //Set tag
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (DateUtils.parse(model.releaseDate) < DateUtils.now()) {
                    binding.tvReleased.text = "Out In Cinemas"
                }
                else {
                    binding.tvReleased.text = "Coming Soon"
                }
            } else {
                binding.tvReleased.text = "Coming Soon"
            }

            //Set up image click listener
            binding.ivGalleryImage.setOnClickListener {
                onMovieCardClick(model.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = GalleryImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(view, onMovieCardClick)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}