package com.example.cryptoapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultsUserModel(
    val avatar: AvatarModel = AvatarModel(),
    val id: Int = 0,
    val iso_639_1: String = "",
    val iso_3166_1: String = "",
    val name: String = "",
    @SerialName("include_adult")
    val includeAdult: Boolean = false,
    val username: String = ""
)