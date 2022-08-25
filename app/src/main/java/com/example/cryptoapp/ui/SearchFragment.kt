package com.example.cryptoapp.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cryptoapp.R
import com.example.cryptoapp.adapter.MovieAdapter
import com.example.cryptoapp.databinding.FragmentSearchBinding
import com.example.cryptoapp.domain.MovieModel
import com.example.cryptoapp.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set up viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.searchViewModel = viewModel

        //Set up adapters
        setUpResultsAdapter()
        setUpSearchFieldAdapter()

        //Set up observers
        setUpObservers()

        //Set up search bar functionality
        setUpSearchBar()

    }

    private fun setUpObservers() {

        //Results
        viewModel.list.observe(viewLifecycleOwner) { newList ->
            (binding.rvResults.adapter as? MovieAdapter)?.submitList(newList)
        }

        //History dropdown
        viewModel.history.observe(viewLifecycleOwner) { newHistory ->
            (binding.acSearchField.adapter as ArrayAdapter<String>).apply {
                clear()
                addAll(newHistory.reversed())
            }
        }
    }

    private fun setUpResultsAdapter() {
        binding.rvResults.layoutManager = GridLayoutManager(activity, 3)

        binding.rvResults.adapter = MovieAdapter(
            { model -> onMovieCardHold(model) },
            { movieId -> onMovieCardClick(movieId) }
        )

    }

    private fun onMovieCardClick(movieId: Int) {
        findNavController().navigate(HomeFragmentDirections.detailsAction(movieId))
    }

    private fun onMovieCardHold(model: MovieModel) {
        viewModel.handleMovieCardHold(model)
    }

    private fun setUpSearchBar() {
        //TextWatcher
        setUpTextWatcher()

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
                    delay(450)
                    viewModel.loadSearchResults(editText.toString())
                }
            }
        })
    }

    private fun setUpSearchFieldAdapter() {
        val searchAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1
        )
        binding.acSearchField.setAdapter(searchAdapter)
    }

    private fun saveNewSearchTerm() {
        val searchTerm = binding.acSearchField.text.toString()
        if (searchTerm.isNotEmpty())
            viewModel.saveNewSearchTerm(searchTerm)

    }
}