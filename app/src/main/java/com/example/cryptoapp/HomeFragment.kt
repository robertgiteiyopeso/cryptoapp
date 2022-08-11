package com.example.cryptoapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mdbRepo = MDBRepositoryRetrofit
    private val dao: MovieDao? by lazy {
        MDBRoomDatabase.getInstance(requireContext())?.getMovieDao()
    }

    private val jobs = mutableListOf<Job>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Display gallery images
        displayGalleryImages()

        //Display actors
        displayActors()

        //Display top rated movies
        displayTopRatedMovies()

        //Display popular movies
        displayPopularMovies()

        //Display movies airing today
        displayAiringMovies()

        //Display Pokemons
        displayPokemons()

        //Set up search button
        setUpSearchButton()

    }

    private fun setUpSearchButton() {
        binding.ivSearchIcon.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container_view_tag, SearchFragment())
                ?.addToBackStack(null)?.commit()
        }
    }

    private fun displayAiringMovies() {
        jobs.add(lifecycleScope.launch(Dispatchers.IO) {
            try {
                //Load movies airing today
                val airingMovies = mdbRepo.getAiringToday("en-US", 1)

                //Update UI
                setUpMovies(airingMovies.results, binding.rvAiring)
            } catch (e: Exception) {
                Log.e("HomeFragment: ", e.message.toString())
            }
        })
    }

    private fun displayPopularMovies() {
        jobs.add(lifecycleScope.launch(Dispatchers.IO) {
            try {
                //Load popular movies
                val popularMovies = mdbRepo.getPopularMovies("en-US", 1)

                //Update UI
                setUpMovies(popularMovies.results, binding.rvPopularMovies)
            } catch (e: Exception) {
                Log.e("HomeFragment: ", e.message.toString())
            }
        })
    }

    private fun displayTopRatedMovies() {
        jobs.add(lifecycleScope.launch(Dispatchers.IO) {
            try {
                //Load top rated movies
                val topRatedMovies = mdbRepo.getTopRatedMovies("en-US", 1)

                //Update UI
                setUpMovies(topRatedMovies.results, binding.rvTopMovies)
            } catch (e: Exception) {
                Log.e("HomeFragment: ", e.message.toString())
            }
        })
    }

    private suspend fun setUpMovies(movieList: List<MovieModel>, view: RecyclerView) {

        val favoriteMovies = dao?.queryAll()
        val movieAdapter = MovieAdapter { model -> onMovieCardHold(model, view) }

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

    private fun displayActors() {
        jobs.add(lifecycleScope.launch(Dispatchers.IO) {
            try {
                //Load actors
                val popularPeople = mdbRepo.getPopularPeople("en-US", 1)

                //Update UI
                launch(Dispatchers.Main) {
                    setUpActors(popularPeople.results)
                }
            } catch (e: Exception) {
                Log.e("HomeFragment: ", e.message.toString())
            }
        })
    }

    private fun displayGalleryImages() {
        jobs.add(lifecycleScope.launch(Dispatchers.IO) {
            try {
                //Load images
                val galleryList =
                    mdbRepo.getTrendingMovies().results.map {
                        GalleryModel(it.backdropPath, it.releaseDate)
                    }.take(6)

                //Update UI
                launch(Dispatchers.Main) {
                    //Update ViewPager Gallery
                    setUpGallery(galleryList)
                    //Set up indicator
                    setUpIndicator(galleryList.size)
                }
            } catch (e: Exception) {
                Log.e("HomeFragment: ", e.message.toString())
            }
        })
    }

    private fun displayPokemons() {
        jobs.add(lifecycleScope.launch(Dispatchers.IO) {
            var pokemonList = listOf<PokemonsQuery.Pokemon?>()
            try {
                val response = apolloClient.query(PokemonsQuery(10)).execute()
                pokemonList = response.data?.pokemons ?: listOf()
            } catch (e: Exception) {
                Log.e("HomeFragment: ", e.toString())
            }

            //Update UI
            launch(Dispatchers.Main) {
                setUpPokemons(pokemonList)
            }
        })
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
        val galleryAdapter = GalleryAdapter()
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
        jobs.forEach { it.cancel() }

        _binding = null
    }
}