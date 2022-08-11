package com.example.cryptoapp.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenModel(
    val success: Boolean = false,
    @SerialName("expires_at")
    val expiresAt: String = "",
    @SerialName("request_token")
    val requestToken: String = "",
    @SerialName("status_code")
    val statusCode: Int = 0,
    @SerialName("status_message")
    val statusMessage: String = ""
)