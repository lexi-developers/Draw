package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.BagProjectDao
import com.example.data.dao.FavoriteColorDao
import com.example.data.model.BagProject
import com.example.data.model.FavoriteColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [BagProject::class, FavoriteColor::class],
  version = 1,
  exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun bagProjectDao(): BagProjectDao
  abstract fun favoriteColorDao(): FavoriteColorDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "leejieun_bags_database",
        )
          .fallbackToDestructiveMigration()
          .addCallback(DatabaseCallback(scope))
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback(
      private val scope: CoroutineScope,
    ) : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateInitialData(database.bagProjectDao(), database.favoriteColorDao())
          }
        }
      }

      suspend fun populateInitialData(bagDao: BagProjectDao, colorDao: FavoriteColorDao) {
        // Initial luxury bag collection projects by Lee Ji-eun (IU Atelier)
        val initialProjects = listOf(
          BagProject(
            id = 1,
            title = "IU 經典珊瑚托特包",
            bagType = "Coral Luxe Tote",
            baseColorHex = "#984061",
            previewImageRes = "iu_coral_tote",
            price = "NT$ 48,000",
            updatedAt = System.currentTimeMillis() - 1000 * 60 * 15,
          ),
          BagProject(
            id = 2,
            title = "李誌恩玫瑰晨露斜背包",
            bagType = "Crossbody Flap",
            baseColorHex = "#F6DDE4",
            previewImageRes = "iu_crossbody_bag",
            price = "NT$ 39,500",
            updatedAt = System.currentTimeMillis() - 1000 * 60 * 60,
          ),
          BagProject(
            id = 3,
            title = "緋紅晚宴珠寶包",
            bagType = "Evening Clasp Bag",
            baseColorHex = "#3E001D",
            previewImageRes = "iu_coral_tote",
            price = "NT$ 56,000",
            updatedAt = System.currentTimeMillis() - 1000 * 60 * 120,
          ),
          BagProject(
            id = 4,
            title = "蜜桃法式馬鞍包",
            bagType = "Saddle Leather Bag",
            baseColorHex = "#FFDBCA",
            previewImageRes = "iu_crossbody_bag",
            price = "NT$ 42,000",
            updatedAt = System.currentTimeMillis() - 1000 * 60 * 300,
          ),
          BagProject(
            id = 5,
            title = "IU 訂製金釦凱莉包",
            bagType = "Kelly Custom Mini",
            baseColorHex = "#74575F",
            previewImageRes = "iu_coral_tote",
            price = "NT$ 62,000",
            updatedAt = System.currentTimeMillis() - 1000 * 60 * 480,
          ),
          BagProject(
            id = 6,
            title = "初秋栗棕隨身手袋",
            bagType = "Petite Pouch",
            baseColorHex = "#82524A",
            previewImageRes = "iu_crossbody_bag",
            price = "NT$ 31,000",
            updatedAt = System.currentTimeMillis() - 1000 * 60 * 720,
          ),
          BagProject(
            id = 7,
            title = "雪絨霧粉水桶包",
            bagType = "Bucket Drawstring",
            baseColorHex = "#FFD9E2",
            previewImageRes = "iu_coral_tote",
            price = "NT$ 45,000",
            updatedAt = System.currentTimeMillis() - 1000 * 60 * 1440,
          ),
          BagProject(
            id = 8,
            title = "IU 簽名版絲絨雲朵包",
            bagType = "Cloud Velvet Clutch",
            baseColorHex = "#984061",
            previewImageRes = "iu_crossbody_bag",
            price = "NT$ 52,000",
            updatedAt = System.currentTimeMillis() - 1000 * 60 * 2880,
          ),
        )

        for (project in initialProjects) {
          bagDao.insertProject(project)
        }

        // Initial favorite leather colors
        val initialFavorites = listOf(
          FavoriteColor(hexCode = "#984061", name = "IU 經典珊瑚紅 IU Coral Luxe"),
          FavoriteColor(hexCode = "#F6DDE4", name = "粉霧初露 Mist Rose"),
          FavoriteColor(hexCode = "#FFDBCA", name = "暖陽蜜桃 Peach Suede"),
          FavoriteColor(hexCode = "#74575F", name = "煙燻紫褐 Velvet Plum"),
          FavoriteColor(hexCode = "#3E001D", name = "深緋暗夜 Night Cherry"),
        )
        for (fav in initialFavorites) {
          colorDao.insertFavorite(fav)
        }
      }
    }
  }
}
