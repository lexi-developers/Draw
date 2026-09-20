package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.BagRepository
import com.example.data.model.BagProject
import com.example.ui.viewmodel.CanvasStroke
import com.example.util.StrokeSerializer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class RoomBagProjectPersistenceTest {

  private lateinit var database: AppDatabase
  private lateinit var repository: BagRepository

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    repository = BagRepository(database.bagProjectDao(), database.favoriteColorDao())
  }

  @After
  fun tearDown() {
    database.close()
  }

  @Test
  fun testCreateSaveReadAndDeleteProject() = runBlocking {
    // 1. Create drawing strokes and serialize
    val testStrokes = listOf(
      CanvasStroke(
        points = listOf(Offset(10f, 20f), Offset(15f, 25f), Offset(30f, 40f)),
        color = Color(0xFF984061),
        strokeWidth = 8f,
        alpha = 1.0f,
        brushType = "細緻描邊筆",
      )
    )
    val serializedJson = StrokeSerializer.serialize(testStrokes)
    assertTrue(serializedJson.isNotBlank())

    // 2. Insert new project (新建)
    val newProject = BagProject(
      id = 0L,
      title = "IU 緋紅星光手袋",
      bagType = "Bespoke Evening Bag",
      baseColorHex = "#984061",
      previewImageRes = "iu_coral_tote",
      price = "NT$ 68,000",
      drawingStrokesJson = serializedJson,
    )
    val insertedId = repository.saveProject(newProject)
    assertTrue(insertedId > 0L)

    // 3. Read project back (讀取)
    val retrieved = repository.getProjectById(insertedId)
    assertNotNull(retrieved)
    assertEquals("IU 緋紅星光手袋", retrieved?.title)
    assertEquals("#984061", retrieved?.baseColorHex)

    // Deserialize strokes and verify data integrity
    val restoredStrokes = StrokeSerializer.deserialize(retrieved?.drawingStrokesJson)
    assertEquals(1, restoredStrokes.size)
    assertEquals(3, restoredStrokes.first().points.size)
    assertEquals(10f, restoredStrokes.first().points[0].x, 0.01f)
    assertEquals(20f, restoredStrokes.first().points[0].y, 0.01f)
    assertEquals("細緻描邊筆", restoredStrokes.first().brushType)

    // 4. Update/Save project (保存更新)
    val updatedProject = retrieved!!.copy(
      title = "IU 緋紅星光手袋 (已定稿)",
      price = "NT$ 72,000",
    )
    repository.saveProject(updatedProject)
    val reFetched = repository.getProjectById(insertedId)
    assertEquals("IU 緋紅星光手袋 (已定稿)", reFetched?.title)
    assertEquals("NT$ 72,000", reFetched?.price)

    // 5. Delete project (刪除)
    repository.deleteProject(reFetched!!)
    val deleted = repository.getProjectById(insertedId)
    assertNull(deleted)

    val all = repository.allProjects.first()
    assertTrue(all.none { it.id == insertedId })
  }
}
