package com.example.cryptoapp

import com.example.cryptoapp.login.Credentials
import com.example.cryptoapp.login.Session
import com.example.cryptoapp.login.Token
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class MDBRepositoryRetrofit(private val apiKey: String) {

    val retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org")
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).build()

    val service = retrofit.create(MDBService::class.java)

    suspend fun getNewTokenParsed(): Token {
        val token = service.getNewTokenParsed(apiKey)
        println("tokenlogin: $token")
        return token
    }

    suspend fun login(credentials: Credentials): Token {
        val token = service.login(apiKey, credentials)
        println("sessiontoken: $token")
        return token
    }

    suspend fun createSession(token: Token): Session {
        val session = service.createSession(apiKey, token)
        println("sessionID: $session")
        return session
    }

    suspend fun invalidateSession(session: Session): Session {
        val session = service.invalidateSession(apiKey, session)
        println("invalidate: $session")
        return session
    }

}