package com.example.cryptoapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultsMovieModel(
    val page: Int = 0,
    val results: List<MovieModel> = emptyList(),
    @SerialName("total_pages")
    val totalPages : Int = 0,
    @SerialName("total_results")
    val totalResults: Int = 0
)
