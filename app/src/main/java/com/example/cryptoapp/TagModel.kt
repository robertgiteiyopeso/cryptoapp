package com.example.cryptoapp

import com.google.gson.annotations.SerializedName

data class TagModel(
    var id: String = "",
    var name: String = "",
    @SerializedName("coin_counter")
    var coinCounter: Int,
    @SerializedName("ico_counter")
    var ico_counter: Int
) {
}