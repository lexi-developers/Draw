package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_colors")
data class FavoriteColor(
  @PrimaryKey
  val hexCode: String,
  val name: String,
  val savedAt: Long = System.currentTimeMillis(),
)
