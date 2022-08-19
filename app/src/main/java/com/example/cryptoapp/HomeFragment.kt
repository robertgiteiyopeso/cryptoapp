package com.example.cryptoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.cryptoapp.adapter.ActorAdapter
import com.example.cryptoapp.adapter.GalleryAdapter
import com.example.cryptoapp.adapter.MovieAdapter
import com.example.cryptoapp.adapter.PokemonAdapter
import com.example.cryptoapp.databinding.FragmentHomeBinding
import com.example.cryptoapp.domain.ActorModel
import com.example.cryptoapp.domain.GalleryModel
import com.example.cryptoapp.domain.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    private val dao: MovieDao? by lazy {
        MDBRoomDatabase.getInstance(requireContext())?.getMovieDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.homeViewModel = viewModel

        //Set up observers
        setUpObservers()

        //Load data
        loadData()

        //Set up search button
        setUpSearchButton()

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
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container_view_tag, SearchFragment())
                ?.addToBackStack(null)?.commit()
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
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container_view_tag, MovieDetailsFragment.newInstance(movieId))
            ?.addToBackStack(null)?.commit()
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