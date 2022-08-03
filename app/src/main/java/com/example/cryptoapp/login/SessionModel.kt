package com.example.cryptoapp.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionModel(
    val success: Boolean = false,
    val failure: Boolean = true,
    @SerialName("session_id")
    val sessionId: String = "",
    @SerialName("status_code")
    val statusCode: Int = 0,
    @SerialName("status_message")
    val statusMessage: String = ""
)