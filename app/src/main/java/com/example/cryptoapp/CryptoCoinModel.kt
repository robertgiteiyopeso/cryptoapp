package com.example.cryptoapp

data class CryptoCoinModel(
    val id: String,
    val name: String,
    val symbol: String,
    val rank: Int,
    val is_new: Boolean,
    val is_active: Boolean,
    val type: String
) {
    override fun toString(): String {
        return "#$rank $symbol ($name), " +
                if (is_new) "NEW, " else "OLD, " +
                if (is_active) "ACTIVE, " else "INACTIVE, " +
                "$type;"
    }
}
