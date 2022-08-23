package com.example.cryptoapp.ancientcryptoapp.domain

data class CryptoCoinModel(
    val id: String = "",
    val name: String = "",
    val symbol: String = "",
    val rank: Int = 0,
    val is_new: Boolean = false,
    val is_active: Boolean = false,
    val type: String = ""
) {
    override fun toString(): String {
        return "#$rank $symbol ($name), " +
                if (is_new) "NEW, " else "OLD, " +
                if (is_active) "ACTIVE, " else "INACTIVE, " +
                "$type;"
    }
}
