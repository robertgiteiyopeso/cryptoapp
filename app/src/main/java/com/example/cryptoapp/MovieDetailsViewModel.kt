package com.example.cryptoapp

import android.util.Log
import androidx.lifecycle.*
import com.example.cryptoapp.domain.ActorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class MovieDetailsViewModel(private val mdbRepo: MDBRepositoryRetrofit) : ViewModel() {

    private var job: Job = Job()

    val movieTitle = MutableLiveData<String>()
    val movieDescription = MutableLiveData<String>()
    val movieImage = MutableLiveData<String>()
    val movieRating = MutableLiveData<String>()

    private val _userAvatar = MutableLiveData<String>()
    val userAvatar: LiveData<String>
        get() = _userAvatar

    private val _actors = MutableLiveData<List<ActorModel>>()
    val actors: LiveData<List<ActorModel>>
        get() = _actors

    init {
        loadUserAvatar()
    }

    private fun loadUserAvatar() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDetails = mdbRepo.getUserDetails()
                _userAvatar.postValue(userDetails.avatar.tmdb.avatarPath)
            } catch (e: Exception) {
                Log.d("MovieDetailsViewModel: ", e.message.toString())
            }
        }
    }

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
                movieRating.postValue(movieDetails.voteAverage.toString())
                _actors.postValue(credits.cast.take(15))
            } catch (e: Exception) {
                Log.e("MovieDetailsViewModel: ", e.toString())
            }
        }
    }
}

class MovieDetailsViewModelFactory(private val application: MovieApplication) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(application.mdbRepo) as T
    }
}
