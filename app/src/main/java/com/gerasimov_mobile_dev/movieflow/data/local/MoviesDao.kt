package com.gerasimov_mobile_dev.movieflow.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies ORDER BY id DESC LIMIT 50") // Берем последние 50 для скорости
    fun getAll(): Flow<List<MovieEntity>>

    @Query("DELETE FROM movies")
    suspend fun deleteAll()
}
