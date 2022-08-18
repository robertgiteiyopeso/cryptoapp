package com.example.cryptoapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.cryptoapp.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieDetailsViewModel by viewModels()

    private lateinit var binding: FragmentMovieDetailsBinding

    private var movieId: Int = 0

    companion object {
        fun newInstance(movieId: Int) = MovieDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt("movie_id", movieId)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt("movie_id")?.let { movieId = it }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.movieDetailsViewModel = viewModel

        viewModel.movieId.postValue(movieId.toString())
    }
}