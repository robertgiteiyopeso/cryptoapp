package com.example.cryptoapp

import com.example.cryptoapp.login.CredentialsModel
import com.example.cryptoapp.login.SessionModel
import com.example.cryptoapp.login.TokenModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class MDBRepositoryRetrofit(private val apiKey: String) {

    val retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org")
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).build()

    val service = retrofit.create(MDBService::class.java)

    suspend fun getNewTokenParsed(): TokenModel = service.getNewTokenParsed(apiKey)

    suspend fun login(credentials: CredentialsModel): TokenModel = service.login(apiKey, credentials)

    suspend fun createSession(token: TokenModel): SessionModel = service.createSession(apiKey, token)

    suspend fun invalidateSession(session: SessionModel): SessionModel = service.invalidateSession(apiKey, session)

}