package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.FavoriteColor
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteColorDao {
  @Query("SELECT * FROM favorite_colors ORDER BY savedAt DESC")
  fun getAllFavorites(): Flow<List<FavoriteColor>>

  @Query("SELECT EXISTS(SELECT 1 FROM favorite_colors WHERE hexCode = :hexCode)")
  fun isFavorite(hexCode: String): Flow<Boolean>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFavorite(color: FavoriteColor)

  @Delete
  suspend fun deleteFavorite(color: FavoriteColor)

  @Query("DELETE FROM favorite_colors WHERE hexCode = :hexCode")
  suspend fun deleteByHex(hexCode: String)
}
