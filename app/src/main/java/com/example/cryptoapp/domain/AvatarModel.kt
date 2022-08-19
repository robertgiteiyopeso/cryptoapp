package com.example.cryptoapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class AvatarModel(
    val gravatar: GravatarModel = GravatarModel(),
    val tmdb: TmdbModel = TmdbModel()
)
