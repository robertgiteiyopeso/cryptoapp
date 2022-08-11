package com.example.cryptoapp

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
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
import androidx.recyclerview.widget.RecyclerView
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
    private val dao: MovieDao? by lazy {
        MDBRoomDatabase.getInstance(requireContext())?.getMovieDao()
    }

    private val sharedPref: SharedPreferences? by lazy {
        activity?.getSharedPreferences(
            "search_history",
            Context.MODE_PRIVATE
        )
    }


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
                val results = (mdbRepo.getSearch("en-US", 1, query).results +
                        mdbRepo.getSearch("en-US", 2, query).results)
                    .filter { it.posterPath.isNotEmpty() }

                //Update UI
                launch(Dispatchers.Main) {
                    displayResults(results)
                }
            } catch (e: Exception) {
                Log.e("LoginFragment: ", e.message.toString())
            }
        }
    }

    private fun displayResults(movieList: List<MovieModel>) {

        val resultGridLayoutManager = GridLayoutManager(activity, 3)
        binding.rvResults.layoutManager = resultGridLayoutManager

        val movieAdapter = MovieAdapter { model -> onMovieCardHold(model, binding.rvResults) }

        lifecycleScope.launch(Dispatchers.IO) {
            val favoriteMovies = dao?.queryAll()

            lifecycleScope.launch(Dispatchers.Main) {
                movieAdapter.list = movieList.map { movie ->
                    if (favoriteMovies?.firstOrNull { it.id == movie.id.toString() } != null) {
                        return@map movie.copy(isFavorite = true)
                    }
                    return@map movie
                }
                binding.rvResults.adapter = movieAdapter
            }
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

    private fun setUpSearchBar() {
        //TextWatcher
        setUpTextWatcher()

        //Set up history dropdown
        setUpHistoryDropdown()

        //Pressing done on the keyboard
        setUpDoneAction()
    }

    private fun setUpDoneAction() {
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

    private fun setUpTextWatcher() {
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
    }

    private fun setUpHistoryDropdown() {
        sharedPref?.apply {
            //Get history as list of search terms
            val history = this.getString(getString(R.string.search_history_10), "")
                ?.split("|")
                ?.dropLast(1)

            //Set up adapter
            if (history != null)
                setUpSearchFieldAdapter(history)
        }
    }

    private fun setUpSearchFieldAdapter(list: List<String>) {
        val searchAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            list.reversed()
        )
        binding.acSearchField.setAdapter(searchAdapter)
    }

    private fun saveNewSearchTerm() {
        sharedPref?.apply {
            //Get new term
            val searchTerm = binding.acSearchField.text.toString()
            if (searchTerm.isEmpty()) return

            //Get old terms
            val history = this.getString(getString(R.string.search_history_10), "")!!

            //Concatenate old terms with new term
            val newHistory = buildString {
                if(history.count{it == '|'} >= 10)
                    append(history.substring(history.indexOf('|') + 1))
                append("$searchTerm|")
            }

            //Save new term
            with(this.edit()) {
                putString(getString(R.string.search_history_10), newHistory)
                apply()
            }

            //Update dropdown list
            setUpSearchFieldAdapter(newHistory.split("|").dropLast(1))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}