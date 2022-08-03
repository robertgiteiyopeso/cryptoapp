package com.example.cryptoapp

import com.example.cryptoapp.domain.ResultsActorModel
import com.example.cryptoapp.domain.ResultsMovieModel
import com.example.cryptoapp.login.CredentialsModel
import com.example.cryptoapp.login.SessionModel
import com.example.cryptoapp.login.TokenModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object MDBRepositoryRetrofit {

    private const val apiKey = "96d31308896f028f63b8801331250f03"
    private val json = Json { coerceInputValues = true }

    @OptIn(ExperimentalSerializationApi::class)
    val retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build()

    val service = retrofit.create(MDBService::class.java)

    suspend fun getNewTokenParsed(): TokenModel = service.getNewTokenParsed(apiKey)

    suspend fun login(credentials: CredentialsModel): TokenModel =
        service.login(apiKey, credentials)

    suspend fun createSession(token: TokenModel): SessionModel =
        service.createSession(apiKey, token)

    suspend fun invalidateSession(session: SessionModel): SessionModel =
        service.invalidateSession(apiKey, session)

    suspend fun getTrendingMovies(): ResultsMovieModel = service.getTrendingMovies(apiKey)

    suspend fun getPopularPeople(language: String, page: Int): ResultsActorModel =
        service.getPopularPeople(apiKey, language, page)

    suspend fun getTopRatedMovies(language: String, page: Int): ResultsMovieModel =
        service.getTopRatedMovies(apiKey, language, page)

    suspend fun getPopularMovies(language: String, page: Int): ResultsMovieModel =
        service.getPopularMovies(apiKey, language, page)

    suspend fun getAiringToday(language: String, page: Int): ResultsMovieModel =
        service.getAiringToday(apiKey, language, page)

}