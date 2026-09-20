package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bag_projects")
data class BagProject(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val title: String,
  val bagType: String,
  val baseColorHex: String,
  val previewImageRes: String,
  val price: String = "NT$ 42,000",
  val drawingStrokesJson: String = "",
  val updatedAt: Long = System.currentTimeMillis(),
)
