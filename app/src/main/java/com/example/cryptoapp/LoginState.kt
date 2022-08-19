package com.example.cryptoapp

sealed class LoginState {
    data class Error(val message: String) : LoginState()
    data class Success(val sessionId: String) : LoginState()
    object InProgress: LoginState()
}
