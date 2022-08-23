package com.example.cryptoapp.login

sealed class LoginState {
    data class Error(val message: String) : LoginState()
    object Success : LoginState()
    object InProgress: LoginState()
}
