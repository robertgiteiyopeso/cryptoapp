package com.example.cryptoapp

import android.content.SharedPreferences
import com.example.android.hilt.di.SharedPreferencesHistory
import com.example.android.hilt.di.SharedPreferencesSession
import com.example.cryptoapp.domain.*
import com.example.cryptoapp.login.CredentialsModel
import com.example.cryptoapp.login.SessionModel
import com.example.cryptoapp.login.TokenModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MDBRepositoryRetrofit @Inject constructor(
    @SharedPreferencesSession
    private val sharedPrefSession: SharedPreferences,
    @SharedPreferencesHistory
    private val sharedPrefHistory: SharedPreferences,
    private val movieDao: MovieDao,
    private val service: MDBService
) {

    companion object {
        private const val apiKey = "96d31308896f028f63b8801331250f03"
    }

    private var sessionId: String? = null

    init {
        sessionId = sharedPrefSession.getString("session_id", "")
    }

    suspend fun getNewTokenParsed(): TokenModel =
        service.getNewTokenParsed(apiKey)

    suspend fun login(credentials: CredentialsModel): TokenModel =
        service.login(apiKey, credentials)

    suspend fun createSession(token: TokenModel): SessionModel {
        val session = service.createSession(apiKey, token)
        sessionId = session.sessionId

        return session
    }

    suspend fun invalidateSession(session: SessionModel): SessionModel =
        service.invalidateSession(apiKey, session)

    suspend fun getTrendingMovies(): ResultsMovieModel =
        service.getTrendingMovies(apiKey)

    suspend fun getPopularPeople(language: String, page: Int): ResultsActorModel =
        service.getPopularPeople(apiKey, language, page)

    suspend fun getTopRatedMovies(language: String, page: Int): ResultsMovieModel =
        service.getTopRatedMovies(apiKey, language, page)

    suspend fun getPopularMovies(language: String, page: Int): ResultsMovieModel =
        service.getPopularMovies(apiKey, language, page)

    suspend fun getAiringToday(language: String, page: Int): ResultsMovieModel =
        service.getAiringToday(apiKey, language, page)

    suspend fun getSearch(language: String, page: Int, query: String): ResultsMovieModel =
        service.getSearch(apiKey, language, page, query)

    suspend fun getMovieById(movieId: String): MovieModel =
        service.getMovieById(apiKey = apiKey, movieId = movieId)

    suspend fun getMovieCredits(movieId: String): ResultsCreditsModel =
        service.getMovieCredits(apiKey = apiKey, movieId = movieId)

    suspend fun getUserDetails(): ResultsUserModel =
        sessionId.let {
            if (it == null)
                throw IllegalStateException("User is not logged in")
            service.getUserDetails(apiKey, it)
        }

    fun isUserLoggedIn(): Boolean = !sessionId.isNullOrBlank()

    fun saveNewSessionId(newId: String) {
        with(sharedPrefSession.edit()) {
            putString("session_id", newId)
            apply()
        }
        sessionId = newId
    }

    fun logout() {
        sessionId = null
        with(sharedPrefSession.edit()) {
            putString("session_id", "")
            commit()
        }
    }

    fun getSearchHistory(): String {
        val history = sharedPrefHistory.getString("search_history_10", "")
        if (history != null)
            return history
        return ""
    }

    fun saveNewSearchTerm(searchTerm: String) {
        //Get old terms
        val history = sharedPrefHistory.getString("search_history_10", "") ?: return

        //Concatenate old terms with new term
        val newHistory = buildString {
            if (history.count { it == '|' } >= 10)
                append(history.substring(history.indexOf('|') + 1))
            append("$searchTerm|")
        }

        //Save new term
        with(sharedPrefHistory.edit()) {
            putString("search_history_10", newHistory)
            apply()
        }
    }

    suspend fun handleMovieCardHold(movie: MovieModel) {
        if (movie.isFavorite) {
            movieDao.deleteById(movie.id.toString())
        } else {
            movieDao.insertOne(MovieDatabaseModel(movie.id.toString(), movie.title))
        }
    }

    suspend fun checkFavoriteMovies(movieList: List<MovieModel>): List<MovieModel> {
        //Get favorite movies
        val favoriteMovies = movieDao.queryAll()

        //Compare results with favorite movies
        return movieList.map { movie ->
            if (favoriteMovies.firstOrNull { it.id == movie.id.toString() } != null) {
                return@map movie.copy(isFavorite = true)
            }
            return@map movie
        }
    }
}