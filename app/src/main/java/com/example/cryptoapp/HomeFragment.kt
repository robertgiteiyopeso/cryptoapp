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
import com.example.cryptoapp.databinding.FragmentHomeBinding
import com.example.cryptoapp.domain.ActorModel
import com.example.cryptoapp.domain.GalleryModel
import com.example.cryptoapp.domain.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mdbRepo = MDBRepositoryRetrofit

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

        //Set up search button
        setUpSearchButton()

    }

    private fun setUpSearchButton() {
        binding.ivSearchIcon.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container_view_tag, SearchFragment())
                ?.commit()
        }
    }

    private fun displayAiringMovies() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                //Load movies airing today
                val airingMovies = mdbRepo.getAiringToday("en-US", 1)

                //Update UI
                launch(Dispatchers.Main) {
                    setUpMovies(airingMovies.results, binding.rvAiring)
                }
            } catch (e: Exception) {
                Log.e("LoginFragment: ", e.message.toString())
            }
        }
    }

    private fun displayPopularMovies() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                //Load popular movies
                val popularMovies = mdbRepo.getPopularMovies("en-US", 1)

                //Update UI
                launch(Dispatchers.Main) {
                    setUpMovies(popularMovies.results, binding.rvPopularMovies)
                }
            } catch (e: Exception) {
                Log.e("LoginFragment: ", e.message.toString())
            }
        }
    }

    private fun displayTopRatedMovies() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                //Load top rated movies
                val topRatedMovies = mdbRepo.getTopRatedMovies("en-US", 1)

                //Update UI
                launch(Dispatchers.Main) {
                    setUpMovies(topRatedMovies.results, binding.rvTopMovies)
                }
            } catch (e: Exception) {
                Log.e("LoginFragment: ", e.message.toString())
            }
        }
    }

    private fun setUpMovies(movieList: List<MovieModel>, view: RecyclerView) {
        val topRatedMovieAdapter = MovieAdapter()
        topRatedMovieAdapter.list = movieList
        view.adapter = topRatedMovieAdapter
    }

    private fun displayActors() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                //Load actors
                val popularPeople = mdbRepo.getPopularPeople("en-US", 1)

                //Update UI
                launch(Dispatchers.Main) {
                    setUpActors(popularPeople.results)
                }
            } catch (e: Exception) {
                Log.e("LoginFragment: ", e.message.toString())
            }
        }
    }

    private fun displayGalleryImages() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                //Load images
                val trending = mdbRepo.getTrendingMovies()
                val galleryList = trending.results.map { GalleryModel(it.backdropPath, it.releaseDate) }.take(6)

                //Update UI
                launch(Dispatchers.Main) {
                    //Update ViewPager Gallery
                    setUpGallery(galleryList)
                    //Set up indicator
                    setUpIndicator(galleryList.size)
                }
            } catch (e: Exception) {
                Log.e("LoginFragment: ", e.message.toString())
            }
        }
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
        _binding = null
    }
}