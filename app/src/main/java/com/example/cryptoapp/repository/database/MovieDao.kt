package com.example.cryptoapp.repository.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<MovieDatabaseModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(data: MovieDatabaseModel)

    @Query("SELECT * FROM $TABLE_NAME WHERE id=:id")
    suspend fun queryById(id: String) : MovieDatabaseModel

    @Query("SELECT * FROM $TABLE_NAME")
    fun queryAll() : Flow<List<MovieDatabaseModel>>

    @Update
    suspend fun update(lastMinuteProduct: MovieDatabaseModel)

    @Query("DELETE FROM $TABLE_NAME WHERE id=:id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

}