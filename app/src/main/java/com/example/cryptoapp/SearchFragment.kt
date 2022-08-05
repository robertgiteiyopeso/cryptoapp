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
import com.example.cryptoapp.databinding.FragmentSearchBinding
import com.google.android.material.internal.TextWatcherAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch
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

    private fun setUpSearchBar() {
        binding.etSearchField.setOnKeyListener(View.OnKeyListener {_, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                loadSearchResults(binding.etSearchField.text.toString())
                return@OnKeyListener true
            }
            false
        })
    }

    private fun loadSearchResults(query: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                //Search for the movies
                val searchResults = mdbRepo.getSearch("en-US", 1, query)

                //Print
                for(movie in searchResults.results)
                    println(movie)
            } catch (e: Exception) {
                Log.e("LoginFragment: ", e.message.toString())
            }
        }
    }

//    private fun setUpSearchBarHelp() {
//        binding.etSearchField.addTextChangedListener(object : TextWatcher {
//
//            var job: Job = Job()
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun afterTextChanged(editText: Editable?) {
//                println("${editText.toString()}")
//
//                job.cancel()
//
//                job = lifecycleScope.launch(Dispatchers.IO) {
//                    println("la somn")
//                    Thread.sleep(1000)
//                    println("trezit")
//                }
//                println("main")
//            }
//        })
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}