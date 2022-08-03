package com.example.cryptoapp

import com.example.cryptoapp.login.CredentialsModel
import com.example.cryptoapp.login.SessionModel
import com.example.cryptoapp.login.TokenModel
import retrofit2.http.*

interface MDBService {

    @GET("/3/authentication/token/new")
    suspend fun getNewTokenParsed(
        @Query("api_key") apiKey: String
    ) : TokenModel

    @POST("/3/authentication/token/validate_with_login")
    suspend fun login(
        @Query("api_key") apiKey: String,
        @Body credentials: CredentialsModel
    ) : TokenModel

    @POST("/3/authentication/session/new")
    suspend fun createSession(
        @Query("api_key") apiKey: String,
        @Body token: TokenModel
    ) : SessionModel

    @HTTP(method = "DELETE", path = "/3/authentication/session", hasBody = true)
    suspend fun invalidateSession(
        @Query("api_key") apiKey: String,
        @Body session: SessionModel
    ) : SessionModel
}