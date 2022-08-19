package com.example.cryptoapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cryptoapp.adapter.ActorAdapter
import com.example.cryptoapp.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieDetailsViewModel by viewModels()

    private lateinit var binding: FragmentMovieDetailsBinding

    private var movieId: Int = 0

    private val sharedPref: SharedPreferences? by lazy {
        activity?.getSharedPreferences(
            "session_id",
            Context.MODE_PRIVATE
        )
    }

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

        //Set up user avatar
        setUpUserAvatar()

        //Display movie details
        viewModel.displayMovieDetails(movieId.toString())
        binding.rvCast.adapter = ActorAdapter()
    }

    private fun setUpUserAvatar() {
        val sessionId = sharedPref?.getString(getString(R.string.session_id), "")

        if (sessionId != null)
            viewModel.loadUserAvatar(sessionId)
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