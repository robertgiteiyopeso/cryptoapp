package com.example.cryptoapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbModel(
    @SerialName("avatar_path")
    val avatarPath: String = ""
)