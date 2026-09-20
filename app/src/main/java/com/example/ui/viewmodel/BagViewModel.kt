package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.BagRepository
import com.example.data.model.BagProject
import com.example.data.model.FavoriteColor
import com.example.util.ColorBlockInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CanvasStroke(
  val points: List<Offset>,
  val color: Color,
  val strokeWidth: Float,
  val alpha: Float = 1.0f,
  val brushType: String = "Leather Paint",
)

data class StudioLayer(
  val id: Int,
  val name: String,
  val isVisible: Boolean = true,
  val isLocked: Boolean = false,
)

class BagViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: BagRepository

  init {
    val database = AppDatabase.getDatabase(application, viewModelScope)
    repository = BagRepository(database.bagProjectDao(), database.favoriteColorDao())
  }

  // Screen Navigation State (1 to 7)
  private val _currentScreen = MutableStateFlow(1)
  val currentScreen: StateFlow<Int> = _currentScreen.asStateFlow()

  // Screen transition direction for animations (1 = forward/slide from right, -1 = backward/slide from left, 0 = fade)
  private val _transitionDirection = MutableStateFlow(1)
  val transitionDirection: StateFlow<Int> = _transitionDirection.asStateFlow()

  private val screenBackStack = mutableListOf<Int>()

  // Data streams
  val allProjects: StateFlow<List<BagProject>> = repository.allProjects
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val recentProjects: StateFlow<List<BagProject>> = repository.recentProjects
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val favoriteColors: StateFlow<List<FavoriteColor>> = repository.favoriteColors
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Search State
  val searchQuery = MutableStateFlow("")
  val searchResults: StateFlow<List<BagProject>> = combine(allProjects, searchQuery) { projects, query ->
    if (query.isBlank()) {
      projects.take(4)
    } else {
      projects.filter {
        it.title.contains(query, ignoreCase = true) ||
          it.bagType.contains(query, ignoreCase = true) ||
          it.baseColorHex.contains(query, ignoreCase = true)
      }
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Studio / Creative Canvas State (Screen 7)
  private val _activeProject = MutableStateFlow<BagProject?>(null)
  val activeProject: StateFlow<BagProject?> = _activeProject.asStateFlow()

  // Tool Modes: 0 = Move/Pan, 1 = Brush/Paint
  val activeTool = MutableStateFlow(1) // default brush
  val isBrushMenuOpen = MutableStateFlow(false)
  val brushType = MutableStateFlow("細緻描邊筆") // Fine Pen, Leather Paint, Marker, Stitch
  val brushColor = MutableStateFlow(Color(0xFF984061)) // Default Coral Primary
  val brushColorHex = MutableStateFlow("#984061")
  val brushOpacity = MutableStateFlow(1.0f)
  val brushSize = MutableStateFlow(8.0f)

  // Canvas strokes & undo/redo
  val strokes = MutableStateFlow<List<CanvasStroke>>(emptyList())
  private val redoStack = mutableListOf<CanvasStroke>()

  // Pan offset for Move tool
  val canvasPanOffset = MutableStateFlow(Offset.Zero)
  val canvasScale = MutableStateFlow(1.0f)

  // Layers
  val layers = MutableStateFlow(
    listOf(
      StudioLayer(1, "底層皮革 (Base Coral Leather)"),
      StudioLayer(2, "金屬五金配件 (Gold Hardware)"),
      StudioLayer(3, "手繪創作圖層 (Sketch & Painting)"),
      StudioLayer(4, "IU 專屬燙金字樣 (Atelier Monogram)"),
    )
  )
  val isLayersMenuOpen = MutableStateFlow(false)
  val isSplitMenuOpen = MutableStateFlow(false)
  val isAttachSheetOpen = MutableStateFlow(false)

  // Screen 4 Color Dialog State
  val selectedColorBlock = MutableStateFlow<ColorBlockInfo?>(null)
  val isColorDialogFavorite = MutableStateFlow(false)

  // Settings State (Screen 5)
  val vipStatus = MutableStateFlow("IU Atelier 尊榮黑金會員")
  val notificationsEnabled = MutableStateFlow(true)
  val autoSaveEnabled = MutableStateFlow(true)
  val gridSnapEnabled = MutableStateFlow(false)
  val highResExport = MutableStateFlow(true)
  val defaultHardware = MutableStateFlow("香檳玫瑰金 (Rose Gold)")

  // UI Events / Snackbars
  private val _uiMessage = MutableSharedFlow<String>()
  val uiMessage: SharedFlow<String> = _uiMessage.asSharedFlow()

  fun navigateTo(screen: Int, direction: Int = 1) {
    if (_currentScreen.value != screen) {
      screenBackStack.add(_currentScreen.value)
      _transitionDirection.value = direction
      _currentScreen.value = screen
    }
  }

  fun navigateBack(): Boolean {
    if (screenBackStack.isNotEmpty()) {
      val prev = screenBackStack.removeAt(screenBackStack.lastIndex)
      _transitionDirection.value = -1
      _currentScreen.value = prev
      return true
    }
    return false
  }

  fun openProjectInStudio(project: BagProject) {
    _activeProject.value = project
    brushColorHex.value = project.baseColorHex
    try {
      val parsedColor = Color(android.graphics.Color.parseColor(project.baseColorHex))
      brushColor.value = parsedColor
    } catch (_: Exception) {
      brushColor.value = Color(0xFF984061)
    }
    // Read and restore strokes from Room persistence
    strokes.value = com.example.util.StrokeSerializer.deserialize(project.drawingStrokesJson)
    redoStack.clear()
    canvasPanOffset.value = Offset.Zero
    canvasScale.value = 1.0f
    navigateTo(7, direction = 1)
  }

  fun createNewProject() {
    val newProj = BagProject(
      id = 0L,
      title = "IU 訂製包包專案 #${(100..999).random()}",
      bagType = "IU Bespoke Handbag",
      baseColorHex = brushColorHex.value,
      previewImageRes = if (Math.random() > 0.5) "iu_coral_tote" else "iu_crossbody_bag",
      price = "NT$ 46,000",
      drawingStrokesJson = "",
    )
    _activeProject.value = newProj
    strokes.value = emptyList()
    redoStack.clear()
    canvasPanOffset.value = Offset.Zero
    canvasScale.value = 1.0f
    navigateTo(7, direction = 1)
  }

  fun saveCurrentProject() {
    val current = _activeProject.value ?: return
    viewModelScope.launch {
      val strokesJson = com.example.util.StrokeSerializer.serialize(strokes.value)
      val updated = current.copy(
        baseColorHex = brushColorHex.value,
        drawingStrokesJson = strokesJson,
        updatedAt = System.currentTimeMillis(),
      )
      val newId = repository.saveProject(updated)
      _activeProject.value = updated.copy(id = if (updated.id == 0L) newId else updated.id)
      _uiMessage.emit("專案「${updated.title}」已成功儲存至資料庫！")
    }
  }

  fun deleteProject(project: BagProject) {
    viewModelScope.launch {
      repository.deleteProject(project)
      if (_activeProject.value?.id == project.id) {
        _activeProject.value = null
        strokes.value = emptyList()
        navigateBack()
      }
      _uiMessage.emit("專案「${project.title}」已從資料庫刪除")
    }
  }

  fun deleteCurrentActiveProject() {
    val current = _activeProject.value ?: return
    deleteProject(current)
  }

  fun addStroke(stroke: CanvasStroke) {
    strokes.value = strokes.value + stroke
    redoStack.clear()
  }

  fun undoStroke() {
    val currentStrokes = strokes.value
    if (currentStrokes.isNotEmpty()) {
      val last = currentStrokes.last()
      redoStack.add(last)
      strokes.value = currentStrokes.dropLast(1)
    }
  }

  fun redoStroke() {
    if (redoStack.isNotEmpty()) {
      val restored = redoStack.removeAt(redoStack.lastIndex)
      strokes.value = strokes.value + restored
    }
  }

  fun toggleTool(tool: Int) {
    if (tool == 1 && activeTool.value == 1) {
      // Second tap on brush opens brush menu as required
      isBrushMenuOpen.value = !isBrushMenuOpen.value
    } else {
      activeTool.value = tool
      if (tool != 1) {
        isBrushMenuOpen.value = false
      }
    }
  }

  fun selectColorForStudio(hex: String, color: Color) {
    brushColorHex.value = hex
    brushColor.value = color
    viewModelScope.launch {
      _uiMessage.emit("已選取皮革繪色：$hex")
    }
  }

  // 10,000+ Colors Dialog & Favorites
  fun openColorDialog(info: ColorBlockInfo) {
    selectedColorBlock.value = info
    val isFav = favoriteColors.value.any { it.hexCode.equals(info.hex, ignoreCase = true) }
    isColorDialogFavorite.value = isFav
  }

  fun toggleColorFavorite(info: ColorBlockInfo) {
    viewModelScope.launch {
      val currentlyFav = favoriteColors.value.any { it.hexCode.equals(info.hex, ignoreCase = true) }
      if (currentlyFav) {
        repository.removeFavoriteColor(info.hex)
        isColorDialogFavorite.value = false
        _uiMessage.emit("已從調色盤喜愛顏色移除")
      } else {
        repository.addFavoriteColor(info.hex, info.name)
        isColorDialogFavorite.value = true
        _uiMessage.emit("已儲存至調色盤喜愛顏色！")
      }
    }
  }

  fun toggleLayerVisibility(layerId: Int) {
    layers.value = layers.value.map {
      if (it.id == layerId) it.copy(isVisible = !it.isVisible) else it
    }
  }
}
