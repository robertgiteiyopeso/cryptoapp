package com.example.cryptoapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class MovieDetailsViewModel : ViewModel() {

    private val mdbRepo = MDBRepositoryRetrofit
    private var job: Job = Job()

    val movieTitle = MutableLiveData<String>()
    val movieDescription = MutableLiveData<String>()
    val movieImage = MutableLiveData<String>()
    val actors = MutableLiveData<String>()

    fun displayMovieDetails(movieId: String) {
        job.cancel()

        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                //Load movie details
                val movieDetails = mdbRepo.getMovieById(movieId)
                val credits = mdbRepo.getMovieCredits(movieId)
                //Update UI
                movieTitle.postValue(movieDetails.title)
                movieDescription.postValue(movieDetails.overview)
                movieImage.postValue(movieDetails.backdropPath)
                actors.postValue(credits.cast.map { it.name }.toString())
            } catch (e: Exception) {
                Log.e("MovieDetailsViewModel: ", e.toString())
            }
        }
    }
}
