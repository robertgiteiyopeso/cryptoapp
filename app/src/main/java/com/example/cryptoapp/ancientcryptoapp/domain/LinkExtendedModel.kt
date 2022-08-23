package com.example.cryptoapp.ancientcryptoapp.domain

data class LinkExtendedModel(
    val url: String = "",
    val type: String = "",
    val stats: StatsModel = StatsModel()
)