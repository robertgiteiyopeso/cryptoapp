package com.example.cryptoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.cryptoapp.domain.GalleryModel
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.databinding.GalleryImageBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class GalleryAdapter() : ListAdapter<GalleryModel, GalleryAdapter.GalleryViewHolder>(object :
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

    inner class GalleryViewHolder(private val binding: GalleryImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: GalleryModel) {
            //Load image
            Glide.with(binding.root.context).load("https://image.tmdb.org/t/p/w500" + model.imageUrl).into(binding.ivGalleryImage)

            //Set tag
            val releaseDate = LocalDate.parse(model.releaseDate)
            val now = LocalDate.now()
            if (releaseDate <= now) {
                println("cinema let's go")
            } else {
                println("no cinema :<")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = GalleryImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}