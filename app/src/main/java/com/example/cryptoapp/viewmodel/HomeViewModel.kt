package com.example.cryptoapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.cryptoapp.repository.MDBRepository
import com.example.cryptoapp.PokemonsQuery
import com.example.cryptoapp.repository.api.apolloClient
import com.example.cryptoapp.domain.ActorModel
import com.example.cryptoapp.domain.GalleryModel
import com.example.cryptoapp.domain.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mdbRepo: MDBRepository
) : ViewModel() {

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

    private val _userAvatar = MutableLiveData<String>()
    val userAvatar: LiveData<String>
        get() = _userAvatar

    init {
        loadUserAvatar()
        loadPopularMovies()
        loadTopRatedMovies()
        loadAiringMovies()
        loadGalleryImages()
        loadPokemons()
        loadActors()
    }

    private fun loadUserAvatar() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDetails = mdbRepo.getUserDetails()
                _userAvatar.postValue(userDetails.avatar.tmdb.avatarPath)
            } catch (e: Exception) {
                Log.d("HomeViewModel: ", e.message.toString())
            }
        }
    }

    private fun loadPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //Load popular movies
                val popularMovies = mdbRepo.getPopularMovies("en-US", 1)

                //Update UI
                mdbRepo.getFavoriteMovies().collect { favoriteMovies ->
                    _popularMovies.postValue(popularMovies.results.map { movie ->
                        if (favoriteMovies.firstOrNull { it.id == movie.id.toString() } != null) {
                            return@map movie.copy(isFavorite = true)
                        }
                        return@map movie
                    })
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel: ", e.message.toString())
            }
        }
    }

    private fun loadTopRatedMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //Load top rated movies
                val topRatedMovies = mdbRepo.getTopRatedMovies("en-US", 1)

                //Update UI
                mdbRepo.getFavoriteMovies().collect { favoriteMovies ->
                    _topRatedMovies.postValue(topRatedMovies.results.map { movie ->
                        if (favoriteMovies.firstOrNull { it.id == movie.id.toString() } != null) {
                            return@map movie.copy(isFavorite = true)
                        }
                        return@map movie
                    })
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel: ", e.message.toString())
            }
        }
    }

    private fun loadAiringMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //Load movies airing today
                val airingMovies = mdbRepo.getAiringToday("en-US", 1)

                //Update UI
                mdbRepo.getFavoriteMovies().collect { favoriteMovies ->
                    _airingMovies.postValue(airingMovies.results.map { movie ->
                        if (favoriteMovies.firstOrNull { it.id == movie.id.toString() } != null) {
                            return@map movie.copy(isFavorite = true)
                        }
                        return@map movie
                    })
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel: ", e.message.toString())
            }
        }
    }

    private fun loadGalleryImages() {
        viewModelScope.launch(Dispatchers.IO) {
            //Load images
            _galleryList.postValue(
                mdbRepo.getTrendingMovies().results.map {
                    GalleryModel(it.id, it.backdropPath, it.releaseDate)
                }.take(6)
            )
        }
    }

    private fun loadActors() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //Load actors
                val popularPeople = mdbRepo.getPopularPeople("en-US", 1)

                //Update UI
                _actorsList.postValue(popularPeople.results)
            } catch (e: Exception) {
                Log.e("HomeViewModel: ", e.message.toString())
            }
        }
    }

    private fun loadPokemons() {
        viewModelScope.launch(Dispatchers.IO) {
            var pokemonList = listOf<PokemonsQuery.Pokemon?>()
            try {
                val response = apolloClient.query(PokemonsQuery(10)).execute()
                pokemonList = response.data?.pokemons ?: listOf()
            } catch (e: Exception) {
                Log.e("HomeViewModel: ", e.toString())
            }

            //Update UI
            _pokemons.postValue(pokemonList)
        }
    }

    fun handleMovieCardHold(movie: MovieModel) {
        viewModelScope.launch(Dispatchers.IO) {
            mdbRepo.handleMovieCardHold(movie)
        }
    }

    fun checkOldLogin() = mdbRepo.isUserLoggedIn()

    fun logout() {
        mdbRepo.logout()
    }
}