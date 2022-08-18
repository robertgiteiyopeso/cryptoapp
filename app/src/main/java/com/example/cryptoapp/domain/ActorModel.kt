package com.example.cryptoapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorModel(
    val adult: Boolean = false,
    val gender: Int = 0,
    val id: Int = 0,
    @SerialName("known_for")
    val knownFor: List<MovieModel> = emptyList(),
    @SerialName("known_for_department")
    val knownForDepartment: String = "",
    val name: String = "",
    val popularity: Double = 0.0,
    @SerialName("profile_path")
    val profilePath: String = "",
    val character: String = ""
)
