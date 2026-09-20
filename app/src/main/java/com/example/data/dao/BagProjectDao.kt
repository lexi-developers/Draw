package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BagProject
import kotlinx.coroutines.flow.Flow

@Dao
interface BagProjectDao {
  @Query("SELECT * FROM bag_projects ORDER BY updatedAt DESC")
  fun getAllProjects(): Flow<List<BagProject>>

  @Query("SELECT * FROM bag_projects ORDER BY updatedAt DESC LIMIT :limit")
  fun getRecentProjects(limit: Int = 9): Flow<List<BagProject>>

  @Query("SELECT * FROM bag_projects WHERE id = :id")
  suspend fun getProjectById(id: Long): BagProject?

  @Query("SELECT * FROM bag_projects WHERE title LIKE '%' || :query || '%' OR bagType LIKE '%' || :query || '%'")
  fun searchProjects(query: String): Flow<List<BagProject>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProject(project: BagProject): Long

  @Update
  suspend fun updateProject(project: BagProject)

  @Delete
  suspend fun deleteProject(project: BagProject)

  @Query("SELECT COUNT(*) FROM bag_projects")
  suspend fun getProjectCount(): Int
}
