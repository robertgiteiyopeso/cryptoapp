package com.example.cryptoapp

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cryptoapp.adapter.MovieAdapter
import com.example.cryptoapp.databinding.FragmentSearchBinding
import com.example.cryptoapp.domain.MovieModel
import kotlinx.coroutines.*
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
        val movieAdapter = MovieAdapter { model -> onMovieCardHold(model) }

        lifecycleScope.launch(Dispatchers.IO) {
            val favoriteMovies = MDBRoomDatabase.getInstance(requireActivity())
                ?.getMovieDao()?.queryAll()

            if (favoriteMovies != null)
                for (favoriteMovie in favoriteMovies) {
                    for (movie in movieList) {
                        if (movie.id.toString() == favoriteMovie.id) {
                            movie.isFavorite = true
                            break
                        }
                    }
                }

            lifecycleScope.launch(Dispatchers.Main) {
                movieAdapter.list = movieList
                binding.rvResults.adapter = movieAdapter
            }
        }
    }

    private fun onMovieCardHold(model: MovieModel) {

        if (model.isFavorite) {
            //</3
            lifecycleScope.launch(Dispatchers.IO) {
                MDBRoomDatabase.getInstance(requireActivity())
                    ?.getMovieDao()?.deleteById(model.id.toString())
            }
        } else {
            //<3
            lifecycleScope.launch(Dispatchers.IO) {
                MDBRoomDatabase.getInstance(requireActivity())
                    ?.getMovieDao()?.insertOne(
                        MovieDatabaseModel(model.id.toString(), model.title)
                    )
            }
        }
    }

    private fun setUpSearchBar() {
        //TextWatcher
        binding.acSearchField.addTextChangedListener(object : TextWatcher {

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

        //Set up history dropdown
        setUpHistoryDropdown()

        //Pressing done on the keyboard
        binding.acSearchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //Hide keyboard
                val inputMethodManager =
                    context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

                //Save search term
                saveNewSearchTerm()

                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun setUpHistoryDropdown() {
        //Get history
        val sharedPref =
            activity?.getSharedPreferences("search_history", Context.MODE_PRIVATE) ?: return

        //Split
        val history = sharedPref.getString(getString(R.string.search_history_10), "")!!
        val historyTerms = history.split("|")
        println("ROBERT: $historyTerms")

        //Set up adapter
        val searchAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            historyTerms.reversed()
        )
        binding.acSearchField.setAdapter(searchAdapter)
    }

    private fun saveNewSearchTerm() {
        val sharedPref =
            activity?.getSharedPreferences("search_history", Context.MODE_PRIVATE) ?: return

        //Get new term
        val searchTerm = binding.acSearchField.text.toString()
        if (searchTerm.isEmpty()) return

        //Get old terms
        var history = sharedPref.getString(getString(R.string.search_history_10), "")!!

        //Concatenate old terms with new term
        val count = history.count { it == '|' }
        if (count >= 10)
            history = history.substring(history.indexOf('|') + 1)
        history += "$searchTerm|"

        //Save new term
        with(sharedPref.edit()) {
            putString(getString(R.string.search_history_10), history)
            apply()
        }

        //Update dropdown list
        val searchAdapter: ArrayAdapter<String> = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                history.split("|").reversed()
            )
        binding.acSearchField.setAdapter(searchAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}