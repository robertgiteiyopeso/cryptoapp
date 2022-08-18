package com.example.cryptoapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class ResultsCreditsModel(
    val id: Int = 0,
    val cast: List<ActorModel> = emptyList(),
    val crew: List<ActorModel> = emptyList()
)
