package com.example.cryptoapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cryptoapp.adapter.GridAdapter
import com.example.cryptoapp.adapter.MovieAdapter
import com.example.cryptoapp.databinding.FragmentSearchBinding
import com.example.cryptoapp.domain.MovieModel
import com.google.android.material.internal.TextWatcherAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import java.lang.Exception

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mdbRepo = MDBRepositoryRetrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set up search bar functionality
        setUpSearchBar()

    }

    private fun loadSearchResults(query: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                //Search for the movies
                val searchResults1 = mdbRepo.getSearch("en-US", 1, query)
                val searchResults2 = mdbRepo.getSearch("en-US", 2, query)

                //Update UI
                launch(Dispatchers.Main) {
                    displayResults((searchResults1.results + searchResults2.results).filter { it.posterPath.isNotEmpty() })
                }
            } catch (e: Exception) {
                Log.e("LoginFragment: ", e.message.toString())
            }
        }
    }

    private fun displayResults(movieList: List<MovieModel>) {
        val resultGridLayoutManager = GridLayoutManager(activity, 3)
        binding.rvResults.layoutManager = resultGridLayoutManager
        val resultsMovieAdapter = MovieAdapter { model -> println(model.name) }
        resultsMovieAdapter.list = movieList
        binding.rvResults.adapter = resultsMovieAdapter
    }

    private fun setUpSearchBar() {
        binding.etSearchField.addTextChangedListener(object : TextWatcher {

            var job: Job = Job()

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editText: Editable?) {
                job.cancel()

                job = lifecycleScope.launch(Dispatchers.IO) {
                    delay(300)
                    loadSearchResults(editText.toString())
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}