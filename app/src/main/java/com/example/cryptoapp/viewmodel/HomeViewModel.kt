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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mdbRepo: MDBRepository
) : ViewModel() {

    private val _pokemons = MutableLiveData<List<PokemonsQuery.Pokemon?>>()
    val pokemons: LiveData<List<PokemonsQuery.Pokemon?>>
        get() = _pokemons

    private val _userAvatar = MutableLiveData<String>()
    val userAvatar: LiveData<String>
        get() = _userAvatar

    private val popularMoviesFlow = flow { emit(mdbRepo.getPopularMovies("en-US", 1).results) }
    val popularMoviesWithFavorites: StateFlow<List<MovieModel>> =
        popularMoviesFlow.combine(mdbRepo.getFavoriteMovies()) { popularMovies, favoriteMovies ->
            popularMovies.map { movie ->
                if (favoriteMovies.firstOrNull { it.id == movie.id.toString() } != null) {
                    return@map movie.copy(isFavorite = true)
                }
                return@map movie
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val topRatedMoviesFlow = flow { emit(mdbRepo.getTopRatedMovies("en-US", 1).results) }
    val topRatedMoviesWithFavorites: StateFlow<List<MovieModel>> =
        topRatedMoviesFlow.combine(mdbRepo.getFavoriteMovies()) { topRatedMovies, favoriteMovies ->
            topRatedMovies.map { movie ->
                if (favoriteMovies.firstOrNull { it.id == movie.id.toString() } != null) {
                    return@map movie.copy(isFavorite = true)
                }
                return@map movie
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val airingMoviesFlow = flow { emit(mdbRepo.getAiringToday("en-US", 1).results) }
    val airingMoviesWithFavorites: StateFlow<List<MovieModel>> =
        airingMoviesFlow.combine(mdbRepo.getFavoriteMovies()) { airingMovies, favoriteMovies ->
            airingMovies.map { movie ->
                if (favoriteMovies.firstOrNull { it.id == movie.id.toString() } != null) {
                    return@map movie.copy(isFavorite = true)
                }
                return@map movie
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val actorsFlow = flow { emit(mdbRepo.getPopularPeople("en-US", 1).results) }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val galleryFlow = flow {
        emit(mdbRepo.getTrendingMovies().results.map {
            GalleryModel(it.id, it.backdropPath, it.releaseDate)
        }.take(6))
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {
        loadUserAvatar()
        loadPokemons()
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