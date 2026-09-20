package com.example.data

import com.example.data.dao.BagProjectDao
import com.example.data.dao.FavoriteColorDao
import com.example.data.model.BagProject
import com.example.data.model.FavoriteColor
import kotlinx.coroutines.flow.Flow

class BagRepository(
  private val bagProjectDao: BagProjectDao,
  private val favoriteColorDao: FavoriteColorDao,
) {
  val allProjects: Flow<List<BagProject>> = bagProjectDao.getAllProjects()
  val recentProjects: Flow<List<BagProject>> = bagProjectDao.getRecentProjects(limit = 9)
  val favoriteColors: Flow<List<FavoriteColor>> = favoriteColorDao.getAllFavorites()

  fun searchProjects(query: String): Flow<List<BagProject>> = bagProjectDao.searchProjects(query)

  suspend fun getProjectById(id: Long): BagProject? = bagProjectDao.getProjectById(id)

  suspend fun saveProject(project: BagProject): Long {
    return if (project.id == 0L) {
      bagProjectDao.insertProject(project)
    } else {
      bagProjectDao.updateProject(project)
      project.id
    }
  }

  suspend fun deleteProject(project: BagProject) {
    bagProjectDao.deleteProject(project)
  }

  suspend fun addFavoriteColor(hexCode: String, name: String) {
    favoriteColorDao.insertFavorite(FavoriteColor(hexCode = hexCode, name = name))
  }

  suspend fun removeFavoriteColor(hexCode: String) {
    favoriteColorDao.deleteByHex(hexCode)
  }

  fun isColorFavorite(hexCode: String): Flow<Boolean> = favoriteColorDao.isFavorite(hexCode)
}
