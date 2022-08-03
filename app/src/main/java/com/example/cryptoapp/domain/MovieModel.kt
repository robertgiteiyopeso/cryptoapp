package com.example.cryptoapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieModel(
    val adult: Boolean = false,
    @SerialName("backdrop_path")
    val backdropPath: String = "",
    val id: Int = 0,
    val title: String = "",
    @SerialName("original_language")
    val originalLanguage: String = "",
    @SerialName("original_title")
    val originalTitle: String = "",
    val overview: String = "",
    @SerialName("poster_path")
    val posterPath: String = "",
    @SerialName("media_type")
    val mediaType: String = "",
    @SerialName("genre_ids")
    val genreIds: List<Int> = emptyList(),
    val popularity: Double = 0.0,
    @SerialName("release_date")
    val releaseDate: String = "",
    val video: Boolean = false,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0,
    //ofc we need more for the ones airing
    @SerialName("first_air_date")
    val firstAirDate: String = "",
    val name: String = "",
    @SerialName("origin_country")
    val originCountry: List<String> = emptyList(),
    @SerialName("original_name")
    val originalName: String = "",
)
