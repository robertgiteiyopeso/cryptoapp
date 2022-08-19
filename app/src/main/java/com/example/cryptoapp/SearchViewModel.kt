package com.example.cryptoapp

import android.util.Log
import androidx.lifecycle.*
import com.example.cryptoapp.domain.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel : ViewModel() {

    private val mdbRepo = MDBRepositoryRetrofit
    private var job: Job = Job()

    private val _list = MutableLiveData<List<MovieModel>>()
    val list: LiveData<List<MovieModel>>
        get() = _list

    fun loadSearchResults(query: String) {
        //Cancel any previous attempt
        job.cancel()

        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                //Search for the movies
                val results = (mdbRepo.getSearch("en-US", 1, query).results +
                        mdbRepo.getSearch("en-US", 2, query).results)
                    .filter { it.posterPath.isNotEmpty() }

                //Update UI
                _list.postValue(results)
            } catch (e: Exception) {
                Log.e("SearchViewModel: ", e.message.toString())
            }
        }
    }
}
