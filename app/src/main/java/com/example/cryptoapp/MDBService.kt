package com.example.cryptoapp

import com.example.cryptoapp.login.Credentials
import com.example.cryptoapp.login.Session
import com.example.cryptoapp.login.Token
import retrofit2.http.*

interface MDBService {

    @GET("/3/authentication/token/new")
    suspend fun getNewTokenParsed(
        @Query("api_key") apiKey: String
    ) : Token

    @POST("/3/authentication/token/validate_with_login")
    suspend fun login(
        @Query("api_key") apiKey: String,
        @Body credentials: Credentials
    ) : Token

    @POST("/3/authentication/session/new")
    suspend fun createSession(
        @Query("api_key") apiKey: String,
        @Body token: Token
    ) : Session

    @HTTP(method = "DELETE", path = "/3/authentication/session", hasBody = true)
    suspend fun invalidateSession(
        @Query("api_key") apiKey: String,
        @Body session: Session
    ) : Session
}