package com.example.cryptoapp

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.example.cryptoapp.domain.ActorModel
import com.example.cryptoapp.domain.GalleryModel
import com.example.cryptoapp.domain.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel(
    private val dao: MovieDao,
    private val mdbRepo: MDBRepositoryRetrofit,
    private val sharedPrefSession: SharedPreferences
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

    private val jobs = mutableListOf<Job>()

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
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            try {
                //Load popular movies
                val popularMovies = mdbRepo.getPopularMovies("en-US", 1)

                //Update UI
                _popularMovies.postValue(checkFavoriteMovies(popularMovies.results))
            } catch (e: Exception) {
                Log.e("HomeViewModel: ", e.message.toString())
            }
        })
    }

    private suspend fun checkFavoriteMovies(movieList: List<MovieModel>): List<MovieModel> {
        //Get favorite movies
        val favoriteMovies = dao.queryAll()

        //Compare results with favorite movies
        return movieList.map { movie ->
            if (favoriteMovies.firstOrNull { it.id == movie.id.toString() } != null) {
                return@map movie.copy(isFavorite = true)
            }
            return@map movie
        }
    }

    private fun loadTopRatedMovies() {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            try {
                //Load top rated movies
                val topRatedMovies = mdbRepo.getTopRatedMovies("en-US", 1)

                //Update UI
                _topRatedMovies.postValue(checkFavoriteMovies(topRatedMovies.results))
            } catch (e: Exception) {
                Log.e("HomeViewModel: ", e.message.toString())
            }
        })
    }

    private fun loadAiringMovies() {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            try {
                //Load movies airing today
                val airingMovies = mdbRepo.getAiringToday("en-US", 1)

                //Update UI
                _airingMovies.postValue(checkFavoriteMovies(airingMovies.results))
            } catch (e: Exception) {
                Log.e("HomeViewModel: ", e.message.toString())
            }
        })
    }

    private fun loadGalleryImages() {
        jobs.add(viewModelScope.launch(Dispatchers.IO) {
            //Load images
            _galleryList.postValue(
                mdbRepo.getTrendingMovies().results.map {
                    GalleryModel(it.id, it.backdropPath, it.releaseDate)
                }.take(6)
            )
        })
    }

    private fun loadActors() {
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

    private fun loadPokemons() {
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

    fun handleMovieCardHold(movie: MovieModel) {
        viewModelScope.launch(Dispatchers.IO) {
            if (movie.isFavorite) {
                dao.deleteById(movie.id.toString())
            } else {
                dao.insertOne(MovieDatabaseModel(movie.id.toString(), movie.title))
            }
        }
    }

    fun cancelJobs() {
        jobs.forEach { it.cancel() }
    }

    fun logout() {
        mdbRepo.sessionId = null
        with(sharedPrefSession.edit()) {
            putString("session_id", "")
            commit()
        }
    }
}

class HomeViewModelFactory(private val application: MovieApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(
            application.dao,
            application.appContainer.mdbRepo,
            application.sharedPrefSession
        ) as T
    }
}