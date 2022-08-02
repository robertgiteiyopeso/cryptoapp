package com.example.cryptoapp.login

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Credentials(
    val username: String = "",
    val password: String = "",
    @SerialName("request_token")
    val requestToken: String = ""
)
