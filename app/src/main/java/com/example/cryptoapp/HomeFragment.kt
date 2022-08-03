package com.example.cryptoapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.databinding.FragmentHomeBinding
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

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val trending = mdbRepo.getTrendingMovies()
                println("got trending: $trending")
                val popularPeople = mdbRepo.getPopularPeople("en-US", 1)
                println("got popular people: $popularPeople")
                val topRatedMovies = mdbRepo.getTopRatedMovies("en-US", 1)
                println("got top rated movies: $topRatedMovies")
                val popularMovies = mdbRepo.getPopularMovies("en-US", 1)
                println("got popular movies: $popularMovies")
                val airing = mdbRepo.getAiringToday("en-US", 1)
                println("got airing: $airing")
            } catch (e: Exception) {
                Log.e("LoginFragment: ", e.message.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}