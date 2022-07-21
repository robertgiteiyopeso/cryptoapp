package com.example.cryptoapp

data class CryptoCoinModel(
    var id: String,
    var name: String,
    var symbol: String,
    var rank: Int,
    var is_new: Boolean,
    var is_active: Boolean,
    var type: String
) {
    override fun toString(): String {
        return "#$rank $symbol ($name), " +
                if (is_new) "NEW, " else "OLD, " +
                if (is_active) "ACTIVE, " else "INACTIVE, " +
                "$type;"
    }
}
