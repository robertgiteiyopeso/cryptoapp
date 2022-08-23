package com.example.cryptoapp

import android.util.Log
import androidx.lifecycle.*
import com.example.cryptoapp.domain.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel(
    private val mdbRepo: MDBRepositoryRetrofit
) :
    ViewModel() {

    private var job: Job = Job()

    private val _list = MutableLiveData<List<MovieModel>>()
    val list: LiveData<List<MovieModel>>
        get() = _list

    private val _history = MutableLiveData<List<String>>()
    val history: LiveData<List<String>>
        get() = _history

    init {
        setUpHistoryDropdown()
    }

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
                _list.postValue(mdbRepo.checkFavoriteMovies(results))
            } catch (e: Exception) {
                Log.e("SearchViewModel: ", e.message.toString())
            }
        }
    }

    private fun setUpHistoryDropdown() {
        _history.postValue(
            mdbRepo.getSearchHistory()
                .split("|")
                .dropLast(1)
        )
    }

    fun saveNewSearchTerm(searchTerm: String) {
        mdbRepo.saveNewSearchTerm(searchTerm)

        //Update dropdown list
        _history.postValue(
            mdbRepo.getSearchHistory()
                .split("|")
                .dropLast(1)
        )
    }

    fun handleMovieCardHold(movie: MovieModel) {
        viewModelScope.launch(Dispatchers.IO) {
            mdbRepo.handleMovieCardHold(movie)
        }
    }
}

class SearchViewModelFactory(private val application: MovieApplication) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            application.mdbRepo
        ) as T
    }
}
