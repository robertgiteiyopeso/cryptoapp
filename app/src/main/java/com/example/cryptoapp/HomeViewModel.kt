package com.example.cryptoapp

import android.util.Log
import androidx.lifecycle.*
import com.example.cryptoapp.domain.ActorModel
import com.example.cryptoapp.domain.GalleryModel
import com.example.cryptoapp.domain.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : ViewModel() {

    private val mdbRepo = MDBRepositoryRetrofit

    private val _galleryList = MutableLiveData<List<GalleryModel>>()
    val galleryList: LiveData<List<GalleryModel>>
        get() = _galleryList

    private val _actorsList = MutableLiveData<List<ActorModel>>()
    val actorsList: LiveData<List<ActorModel>>
        get() = _actorsList

    private val _popularMovies = MutableLiveData<List<MovieModel>>()
    val popularMovies: LiveData<List<MovieModel>>
        get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<List<MovieModel>>()
    val topRatedMovies: LiveData<List<MovieModel>>
        get() = _topRatedMovies

    private val _airingMovies = MutableLiveData<List<MovieModel>>()
    val airingMovies: LiveData<List<MovieModel>>
        get() = _airingMovies

    private val _pokemons = MutableLiveData<List<PokemonsQuery.Pokemon?>>()
    val pokemons: LiveData<List<PokemonsQuery.Pokemon?>>
        get() = _pokemons

    val userAvatar = MutableLiveData<String>()

    //TODO zice cu NoSuchMethodError
//    private val _userAvatar = MutableLiveData<String>()
//    val userAvatar: LiveData<String>
//        get() = _userAvatar

    private val jobs = mutableListOf<Job>()

    fun loadUserAvatar(sessionId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDetails = MDBRepositoryRetrofit.getUserDetails(sessionId)
                userAvatar.postValue(userDetails.avatar.tmdb.avatarPath)
            } catch (e: Exception) {
                Log.d("HomeViewModel: ", e.message.toString())
            }
        }
    }

    fun loadPopularMovies() {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            try {
                //Load popular movies
                val popularMovies = mdbRepo.getPopularMovies("en-US", 1)

                //Update UI
                _popularMovies.postValue(popularMovies.results)
            } catch (e: Exception) {
                Log.e("HomeViewModel: ", e.message.toString())
            }
        })
    }

    fun loadTopRatedMovies() {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            try {
                //Load top rated movies
                val topRatedMovies = mdbRepo.getTopRatedMovies("en-US", 1)

                //Update UI
                _topRatedMovies.postValue(topRatedMovies.results)
            } catch (e: Exception) {
                Log.e("HomeViewModel: ", e.message.toString())
            }
        })
    }

    fun loadAiringMovies() {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            try {
                //Load movies airing today
                val airingMovies = mdbRepo.getAiringToday("en-US", 1)

                //Update UI
                _airingMovies.postValue(airingMovies.results)
            } catch (e: Exception) {
                Log.e("HomeViewModel: ", e.message.toString())
            }
        })
    }

    fun loadGalleryImages() {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            //Load images
            _galleryList.postValue(
                mdbRepo.getTrendingMovies().results.map {
                    GalleryModel(it.id, it.backdropPath, it.releaseDate)
                }.take(6)
            )
        })
    }

    fun loadActors() {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            try {
                //Load actors
                val popularPeople = mdbRepo.getPopularPeople("en-US", 1)

                //Update UI
                _actorsList.postValue(popularPeople.results)
            } catch (e: Exception) {
                Log.e("HomeViewModel: ", e.message.toString())
            }
        })
    }

    fun loadPokemons() {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            var pokemonList = listOf<PokemonsQuery.Pokemon?>()
            try {
                val response = apolloClient.query(PokemonsQuery(10)).execute()
                pokemonList = response.data?.pokemons ?: listOf()
            } catch (e: Exception) {
                Log.e("HomeViewModel: ", e.toString())
            }

            //Update UI
            _pokemons.postValue(pokemonList)
        })
    }

    fun cancelJobs() {
        jobs.forEach { it.cancel() }
    }
}