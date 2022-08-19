package com.example.cryptoapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class GravatarModel(
    val hash: String = ""
)