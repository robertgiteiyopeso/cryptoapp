package com.example.cryptoapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.cryptoapp.repository.MDBRepository
import com.example.cryptoapp.domain.ActorModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val mdbRepo: MDBRepository
) : ViewModel() {

    private var job: Job = Job()

    private val _movieTitle = MutableLiveData<String>()
    val movieTitle: LiveData<String>
        get() = _movieTitle

    private val _movieDescription = MutableLiveData<String>()
    val movieDescription: LiveData<String>
        get() = _movieDescription

    private val _movieRating = MutableLiveData<String>()
    val movieRating: LiveData<String>
        get() = _movieRating

    private val _movieImage = MutableLiveData<String>()
    val movieImage: LiveData<String>
        get() = _movieImage

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
                _movieTitle.postValue(movieDetails.title)
                _movieDescription.postValue(movieDetails.overview)
                _movieImage.postValue(movieDetails.backdropPath)
                _movieRating.postValue(movieDetails.voteAverage.toString())
                _actors.postValue(credits.cast.take(15))
            } catch (e: Exception) {
                Log.e("MovieDetailsViewModel: ", e.toString())
            }
        }
    }
}
