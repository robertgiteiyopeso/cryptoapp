package com.example.cryptoapp

sealed class LoginState {
    data class Error(val message: String) : LoginState()
    object Success : LoginState()
    object InProgress: LoginState()
}
