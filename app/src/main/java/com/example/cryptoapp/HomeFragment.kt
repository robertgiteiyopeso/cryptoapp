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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.cryptoapp.adapter.ActorAdapter
import com.example.cryptoapp.adapter.GalleryAdapter
import com.example.cryptoapp.adapter.MovieAdapter
import com.example.cryptoapp.adapter.PokemonAdapter
import com.example.cryptoapp.databinding.FragmentHomeBinding
import com.example.cryptoapp.domain.ActorModel
import com.example.cryptoapp.domain.GalleryModel
import com.example.cryptoapp.domain.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels{HomeViewModelFactory(requireContext().applicationContext as MovieApplication)}

    private lateinit var binding: FragmentHomeBinding

    //TODO cum fac asta sa fie in viewmodel?
    private val dao: MovieDao? by lazy {
        MDBRoomDatabase.getInstance(requireContext())?.getMovieDao()
    }

    //TODO cum fac si asta sa fie in viewmodel?
//    private val sharedPref: SharedPreferences? by lazy {
//        activity?.getSharedPreferences(
//            "session_id",
//            Context.MODE_PRIVATE
//        )
//    }

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


        //Set up observers
        setUpObservers()

        //Load data
        loadData()

        //Set up search button
        setUpSearchButton()

        //Set up user avatar
        setUpUserAvatar()

    }

    private fun setUpUserAvatar() {
        val sessionId = viewModel.sharedPref.getString(getString(R.string.session_id), "")

        if (sessionId != null)
            viewModel.loadUserAvatar()
    }

    private fun loadData() {
        //Gallery images
        viewModel.loadGalleryImages()

        //Actors
        viewModel.loadActors()

        //Top rated movies
        viewModel.loadTopRatedMovies()

        //Popular movies
        viewModel.loadPopularMovies()

        //Movies airing today
        viewModel.loadAiringMovies()

        //Pokemons
        viewModel.loadPokemons()
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
        viewModel.galleryList.observe(viewLifecycleOwner) { newList ->
            setUpGallery(newList)
            setUpIndicator(newList.size)
        }

        //Actors
        viewModel.actorsList.observe(viewLifecycleOwner) { newList ->
            setUpActors(newList)
        }

        //Top rated movies
        viewModel.topRatedMovies.observe(viewLifecycleOwner) { newList ->
            setUpMovies(newList, binding.rvTopMovies)
        }

        //Popular movies
        viewModel.popularMovies.observe(viewLifecycleOwner) { newList ->
            setUpMovies(newList, binding.rvPopularMovies)
        }

        //Movies airing today
        viewModel.airingMovies.observe(viewLifecycleOwner) { newList ->
            setUpMovies(newList, binding.rvAiring)
        }

        //Pokemons
        viewModel.pokemons.observe(viewLifecycleOwner) { newList ->
            setUpPokemons(newList)
        }
    }

    private fun setUpSearchButton() {
        binding.ivSearchIcon.setOnClickListener {
            findNavController().navigate(R.id.search_action)
        }
    }

    private fun setUpMovies(movieList: List<MovieModel>, view: RecyclerView) {

        lifecycleScope.launch(Dispatchers.IO) {
            val favoriteMovies = dao?.queryAll()
            val movieAdapter = MovieAdapter(
                { model -> onMovieCardHold(model, view) },
                { movieId -> onMovieCardClick(movieId) }
            )

            lifecycleScope.launch(Dispatchers.Main) {
                movieAdapter.list = movieList.map { movie ->
                    if (favoriteMovies?.firstOrNull { it.id == movie.id.toString() } != null) {
                        return@map movie.copy(isFavorite = true)
                    }
                    return@map movie
                }
                view.adapter = movieAdapter
            }
        }
    }

    private fun onMovieCardClick(movieId: Int) {
        findNavController().navigate(HomeFragmentDirections.detailsAction(movieId))
    }

    private fun onMovieCardHold(model: MovieModel, view: RecyclerView) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (model.isFavorite) {
                dao?.deleteById(model.id.toString())
            } else {
                dao?.insertOne(MovieDatabaseModel(model.id.toString(), model.title))
            }
        }
        (view.adapter as? MovieAdapter)?.modifyOneElement(model)
    }

    private fun setUpPokemons(pokemonList: List<PokemonsQuery.Pokemon?>) {
        val pokemonAdapter = PokemonAdapter()
        pokemonAdapter.list = pokemonList
        binding.rvPokemons.adapter = pokemonAdapter
    }

    private fun setUpActors(actorList: List<ActorModel>) {
        val actorAdapter = ActorAdapter()
        actorAdapter.list = actorList
        binding.rvStars.adapter = actorAdapter
    }

    private fun setUpGallery(galleryList: List<GalleryModel>) {
        val galleryAdapter = GalleryAdapter { movieId ->
            onMovieCardClick(movieId)
        }
        galleryAdapter.submitList(galleryList)
        binding.vpGallery.adapter = galleryAdapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cancelJobs()
    }
}