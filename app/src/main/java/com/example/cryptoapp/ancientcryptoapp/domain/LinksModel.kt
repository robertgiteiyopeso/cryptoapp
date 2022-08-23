package com.example.cryptoapp.ancientcryptoapp.domain

import com.google.gson.annotations.SerializedName

data class LinksModel(
    val explorer: List<String> = emptyList(),
    val facebook: List<String> = emptyList(),
    val reddit: List<String> = emptyList(),
    @SerializedName("source_code")
    val sourceCode: List<String> = emptyList(),
    val website: List<String> = emptyList(),
    val youtube: List<String> = emptyList(),
)