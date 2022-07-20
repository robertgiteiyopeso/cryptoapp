package com.example.cryptoapp

data class CryptoCoin(
    var id: String,
    var name: String,
    var symbol: String,
    var rank : Int,
    var is_new: Boolean,
    var is_active: Boolean,
    var type: String
)
