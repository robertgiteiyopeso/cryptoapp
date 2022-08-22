package com.example.cryptoapp

import com.example.cryptoapp.domain.*
import com.example.cryptoapp.login.CredentialsModel
import com.example.cryptoapp.login.SessionModel
import com.example.cryptoapp.login.TokenModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class MDBRepositoryRetrofit {

    private val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    companion object {
        private const val apiKey = "96d31308896f028f63b8801331250f03"
    }

    var sessionId: String? = null

    @OptIn(ExperimentalSerializationApi::class)
    val retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build()

    private val service = retrofit.create(MDBService::class.java)

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
}