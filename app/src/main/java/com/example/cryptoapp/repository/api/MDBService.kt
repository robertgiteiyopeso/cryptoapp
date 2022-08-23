package com.example.cryptoapp.repository.api

import com.example.cryptoapp.domain.*
import com.example.cryptoapp.login.CredentialsModel
import com.example.cryptoapp.login.SessionModel
import com.example.cryptoapp.login.TokenModel
import retrofit2.http.*

interface MDBService {

    @GET("/3/authentication/token/new")
    suspend fun getNewTokenParsed(
        @Query("api_key") apiKey: String
    ): TokenModel

    @POST("/3/authentication/token/validate_with_login")
    suspend fun login(
        @Query("api_key") apiKey: String,
        @Body credentials: CredentialsModel
    ): TokenModel

    @POST("/3/authentication/session/new")
    suspend fun createSession(
        @Query("api_key") apiKey: String,
        @Body token: TokenModel
    ): SessionModel

    @HTTP(method = "DELETE", path = "/3/authentication/session", hasBody = true)
    suspend fun invalidateSession(
        @Query("api_key") apiKey: String,
        @Body session: SessionModel
    ): SessionModel

    @GET("/3/trending/all/day")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String
    ): ResultsMovieModel

    @GET("/3/person/popular")
    suspend fun getPopularPeople(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): ResultsActorModel

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): ResultsMovieModel

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): ResultsMovieModel

    @GET("/3/tv/airing_today")
    suspend fun getAiringToday(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): ResultsMovieModel

    @GET("/3/search/movie")
    suspend fun getSearch(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): ResultsMovieModel

    @GET("3/movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): MovieModel

    @GET("/3/movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): ResultsCreditsModel

    @GET("/3/account")
    suspend fun getUserDetails(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): ResultsUserModel

}