package com.example.cryptoapp.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME = "favorite_movies"

@Entity(tableName = TABLE_NAME)
data class MovieDatabaseModel(
    @PrimaryKey
    @ColumnInfo
    val id: String = "",
    @ColumnInfo
    val name: String = ""
)
