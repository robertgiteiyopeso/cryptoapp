package com.example.cryptoapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.adapter.ActorAdapter
import com.example.cryptoapp.adapter.GalleryAdapter
import com.example.cryptoapp.adapter.MovieAdapter
import com.example.cryptoapp.adapter.PokemonAdapter
import com.example.cryptoapp.databinding.FragmentHomeBinding
import com.example.cryptoapp.domain.MovieModel
import com.example.cryptoapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set up viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.homeViewModel = viewModel

        if(!viewModel.checkOldLogin())
            findNavController().navigate(
                R.id.login_action,
                null,
                navOptions { popUpTo(R.id.home_fragment) { inclusive = true } })

        //Set up adapters
        setUpAdapters()

        //Set up observers
        setUpObservers()

        //Set up search button
        setUpSearchButton()

        //Set up logout on avatar image
        setUpLogout()

    }

    private fun setUpLogout() {
        binding.ivUserPhoto.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(
                R.id.login_action,
                null,
                navOptions { popUpTo(R.id.home_fragment) { inclusive = true } })
        }
    }

    private fun setUpAdapters() {

        //Gallery
        binding.vpGallery.adapter = GalleryAdapter { movieId ->
            onMovieCardClick(movieId)
        }

        //Actors
        binding.rvStars.adapter = ActorAdapter()

        //Movies
        listOf(binding.rvTopMovies, binding.rvPopularMovies, binding.rvAiring).forEach {
            it.adapter = MovieAdapter(
                { model -> onMovieCardHold(model) },
                { movieId -> onMovieCardClick(movieId) }
            )
        }

        //Pokemons
        binding.rvPokemons.adapter = PokemonAdapter()
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

        //Gallery images
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.galleryFlow.collect {
                    (binding.vpGallery.adapter as GalleryAdapter).submitList(it)
                    setUpIndicator(it.size)
                }
            }
        }

        //Actors
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actorsFlow.collect {
                    (binding.rvStars.adapter as ActorAdapter).list = it
                }
            }
        }

        //Top rated movies
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.topRatedMoviesWithFavorites.collect {
                    (binding.rvTopMovies.adapter as MovieAdapter).submitList(it)
                }
            }
        }

        //Popular movies
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.popularMoviesWithFavorites.collect {
                    (binding.rvPopularMovies.adapter as MovieAdapter).submitList(it)
                }
            }
        }

        //Movies airing today
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.airingMoviesWithFavorites.collect {
                    (binding.rvAiring.adapter as MovieAdapter).submitList(it)
                }
            }
        }

        //Pokemons
        viewModel.pokemons.observe(viewLifecycleOwner) { newList ->
            (binding.rvPokemons.adapter as PokemonAdapter).list = newList
        }
    }

    private fun setUpSearchButton() {
        binding.ivSearchIcon.setOnClickListener {
            findNavController().navigate(R.id.search_action)
        }
    }

    private fun onMovieCardClick(movieId: Int) {
        findNavController().navigate(
            HomeFragmentDirections.detailsAction(
                movieId
            )
        )
    }

    private fun onMovieCardHold(model: MovieModel) {
        viewModel.handleMovieCardHold(model)
    }

    private fun setUpIndicator(size: Int) {
        binding.vpGallery.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                binding.ivGalleryIndicator.onPageScrollStateChanged(state)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.ivGalleryIndicator.onPageSelected(position)
            }
        })
        binding.ivGalleryIndicator.setPageSize(
            size
        )
        binding.ivGalleryIndicator.notifyDataChanged()
    }
}