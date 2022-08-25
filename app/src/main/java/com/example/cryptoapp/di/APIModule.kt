package com.example.cryptoapp.di

import com.example.cryptoapp.repository.api.MDBService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
class APIModule {

    @Provides
    fun provideMDBService(retrofit: Retrofit): MDBService =
        retrofit.create(MDBService::class.java)

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideRetrofit(json: Json): Retrofit =
        Retrofit.Builder().baseUrl("https://api.themoviedb.org")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    fun provideJson(): Json =
        Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
}