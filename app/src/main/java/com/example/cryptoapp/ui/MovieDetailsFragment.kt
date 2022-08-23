package com.example.cryptoapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.adapter.ActorAdapter
import com.example.cryptoapp.databinding.FragmentMovieDetailsBinding
import com.example.cryptoapp.viewmodel.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieDetailsViewModel by viewModels()

    private lateinit var binding: FragmentMovieDetailsBinding

    private var movieId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)
        val safeArgs: MovieDetailsFragmentArgs by navArgs()
        movieId = safeArgs.movieId
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set up viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.movieDetailsViewModel = viewModel

        //Set up observers
        setUpObservers()

        //Display movie details
        viewModel.displayMovieDetails(movieId.toString())

        //Set up cast adapter
        binding.rvCast.adapter = ActorAdapter()
    }

    private fun setUpObservers() {

        //User avatar
        viewModel.userAvatar.observe(viewLifecycleOwner) { newImage ->
            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w500$newImage")
                .placeholder(R.drawable.logo)
                .circleCrop()
                .into(binding.ivUserPhoto)
        }

        //Movie image
        viewModel.movieImage.observe(viewLifecycleOwner) { newImage ->
            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w500$newImage")
                .into(binding.ivMovieImage)
        }

        //Cast
        viewModel.actors.observe(viewLifecycleOwner) { newList ->
            (binding.rvCast.adapter as? ActorAdapter)?.list = newList
        }
    }
}