package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.Screen1Loading
import com.example.ui.screens.Screen2Home
import com.example.ui.screens.Screen3Projects
import com.example.ui.screens.Screen4Palette
import com.example.ui.screens.Screen5Settings
import com.example.ui.screens.Screen6Search
import com.example.ui.screens.Screen7Studio
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.BagViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
  private val viewModel: BagViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MainAppContent(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun MainAppContent(viewModel: BagViewModel) {
  val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
  val transitionDirection by viewModel.transitionDirection.collectAsStateWithLifecycle()
  val allProjects by viewModel.allProjects.collectAsStateWithLifecycle()
  val recentProjects by viewModel.recentProjects.collectAsStateWithLifecycle()
  val favoriteColors by viewModel.favoriteColors.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
  val activeProject by viewModel.activeProject.collectAsStateWithLifecycle()

  // Studio states
  val activeTool by viewModel.activeTool.collectAsStateWithLifecycle()
  val isBrushMenuOpen by viewModel.isBrushMenuOpen.collectAsStateWithLifecycle()
  val brushType by viewModel.brushType.collectAsStateWithLifecycle()
  val brushColor by viewModel.brushColor.collectAsStateWithLifecycle()
  val brushOpacity by viewModel.brushOpacity.collectAsStateWithLifecycle()
  val brushSize by viewModel.brushSize.collectAsStateWithLifecycle()
  val strokes by viewModel.strokes.collectAsStateWithLifecycle()
  val canvasPanOffset by viewModel.canvasPanOffset.collectAsStateWithLifecycle()
  val layers by viewModel.layers.collectAsStateWithLifecycle()
  val isLayersMenuOpen by viewModel.isLayersMenuOpen.collectAsStateWithLifecycle()
  val isSplitMenuOpen by viewModel.isSplitMenuOpen.collectAsStateWithLifecycle()
  val isAttachSheetOpen by viewModel.isAttachSheetOpen.collectAsStateWithLifecycle()

  // Color dialog state
  val selectedColorBlock by viewModel.selectedColorBlock.collectAsStateWithLifecycle()
  val isColorFavorite by viewModel.isColorDialogFavorite.collectAsStateWithLifecycle()

  // Settings states
  val notificationsEnabled by viewModel.notificationsEnabled.collectAsStateWithLifecycle()
  val autoSaveEnabled by viewModel.autoSaveEnabled.collectAsStateWithLifecycle()
  val gridSnapEnabled by viewModel.gridSnapEnabled.collectAsStateWithLifecycle()
  val highResExport by viewModel.highResExport.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }

  // Listen to UI messages
  LaunchedEffect(Unit) {
    viewModel.uiMessage.collectLatest { msg ->
      snackbarHostState.showSnackbar(msg)
    }
  }

  // Handle system back gesture
  BackHandler(enabled = currentScreen > 2) {
    viewModel.navigateBack()
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.surface,
    snackbarHost = { SnackbarHost(snackbarHostState) },
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
    ) {
      AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
          when {
            // Screen 6 (Search): fade in transition
            targetState == 6 || initialState == 6 -> {
              fadeIn(animationSpec = tween(280)) togetherWith fadeOut(animationSpec = tween(280))
            }
            // Transition from left to Screen 5 as requested: "從左側滑入"
            targetState == 5 && transitionDirection == -1 -> {
              slideInHorizontally(
                animationSpec = tween(300, easing = FastOutSlowInEasing),
                initialOffsetX = { -it },
              ) + fadeIn() togetherWith slideOutHorizontally(
                animationSpec = tween(300, easing = FastOutSlowInEasing),
                targetOffsetX = { it },
              ) + fadeOut()
            }
            // Transition to Screen 2 or back: reverse slide
            transitionDirection == -1 -> {
              slideInHorizontally(
                animationSpec = tween(300, easing = FastOutSlowInEasing),
                initialOffsetX = { -it },
              ) + fadeIn() togetherWith slideOutHorizontally(
                animationSpec = tween(300, easing = FastOutSlowInEasing),
                targetOffsetX = { it },
              ) + fadeOut()
            }
            // Default forward slide: from right
            else -> {
              slideInHorizontally(
                animationSpec = tween(300, easing = FastOutSlowInEasing),
                initialOffsetX = { it },
              ) + fadeIn() togetherWith slideOutHorizontally(
                animationSpec = tween(300, easing = FastOutSlowInEasing),
                targetOffsetX = { -it },
              ) + fadeOut()
            }
          }
        },
        label = "screen_transition",
      ) { targetScreen ->
        when (targetScreen) {
          1 -> Screen1Loading(
            onLoadingComplete = { viewModel.navigateTo(2, direction = 1) },
          )
          2 -> Screen2Home(
            recentProjects = recentProjects,
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.searchQuery.value = it },
            onSearchClick = { viewModel.navigateTo(6, direction = 0) }, // fade to Screen 6
            onProjectClick = { project -> viewModel.openProjectInStudio(project) },
            onAddProjectClick = { viewModel.createNewProject() },
            onNavigateToTab = { tab ->
              when (tab) {
                0 -> viewModel.navigateTo(2, direction = 1)
                1 -> viewModel.navigateTo(3, direction = 1)
                2 -> viewModel.navigateTo(4, direction = 1)
                3 -> viewModel.navigateTo(5, direction = -1) // slide from left to settings
              }
            },
          )
          3 -> Screen3Projects(
            projects = allProjects,
            onProjectClick = { project -> viewModel.openProjectInStudio(project) },
            onAddProjectClick = { viewModel.createNewProject() },
            onDeleteProject = { project -> viewModel.deleteProject(project) },
            onNavigateToTab = { tab ->
              when (tab) {
                0 -> viewModel.navigateTo(2, direction = 1)
                1 -> viewModel.navigateTo(3, direction = 1)
                2 -> viewModel.navigateTo(4, direction = 1)
                3 -> viewModel.navigateTo(5, direction = -1)
              }
            },
          )
          4 -> Screen4Palette(
            favoriteColors = favoriteColors,
            selectedColorBlock = selectedColorBlock,
            isColorFavorite = isColorFavorite,
            onColorBlockClick = { info -> viewModel.openColorDialog(info) },
            onDismissDialog = { viewModel.selectedColorBlock.value = null },
            onToggleFavorite = { info -> viewModel.toggleColorFavorite(info) },
            onApplyColorToStudio = { hex, color ->
              viewModel.selectColorForStudio(hex, color)
              viewModel.navigateTo(7, direction = 1)
            },
            onAddProjectClick = { viewModel.createNewProject() },
            onNavigateToTab = { tab ->
              when (tab) {
                0 -> viewModel.navigateTo(2, direction = 1)
                1 -> viewModel.navigateTo(3, direction = 1)
                2 -> viewModel.navigateTo(4, direction = 1)
                3 -> viewModel.navigateTo(5, direction = -1)
              }
            },
          )
          5 -> Screen5Settings(
            notificationsEnabled = notificationsEnabled,
            onNotificationsChange = { viewModel.notificationsEnabled.value = it },
            autoSaveEnabled = autoSaveEnabled,
            onAutoSaveChange = { viewModel.autoSaveEnabled.value = it },
            gridSnapEnabled = gridSnapEnabled,
            onGridSnapChange = { viewModel.gridSnapEnabled.value = it },
            highResExport = highResExport,
            onHighResExportChange = { viewModel.highResExport.value = it },
            onAddProjectClick = { viewModel.createNewProject() },
            onNavigateToTab = { tab ->
              when (tab) {
                0 -> viewModel.navigateTo(2, direction = 1)
                1 -> viewModel.navigateTo(3, direction = 1)
                2 -> viewModel.navigateTo(4, direction = 1)
                3 -> viewModel.navigateTo(5, direction = 1)
              }
            },
          )
          6 -> Screen6Search(
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.searchQuery.value = it },
            searchResults = searchResults,
            onCloseClick = { viewModel.navigateBack() },
            onProjectClick = { project -> viewModel.openProjectInStudio(project) },
            onDeleteProject = { project -> viewModel.deleteProject(project) },
          )
          7 -> Screen7Studio(
            activeProject = activeProject,
            strokes = strokes,
            favoriteColors = favoriteColors,
            activeTool = activeTool,
            isBrushMenuOpen = isBrushMenuOpen,
            brushType = brushType,
            brushColor = brushColor,
            brushOpacity = brushOpacity,
            brushSize = brushSize,
            canvasPanOffset = canvasPanOffset,
            layers = layers,
            isLayersMenuOpen = isLayersMenuOpen,
            isSplitMenuOpen = isSplitMenuOpen,
            isAttachSheetOpen = isAttachSheetOpen,
            onToolSelect = { tool -> viewModel.toggleTool(tool) },
            onUndo = { viewModel.undoStroke() },
            onRedo = { viewModel.redoStroke() },
            onSaveClick = { viewModel.saveCurrentProject() },
            onCloseWithoutSaving = { viewModel.navigateBack() },
            onShare = {
              viewModel.viewModelScopeLaunchShare()
            },
            onLayersToggle = { viewModel.isLayersMenuOpen.value = it },
            onSplitMenuToggle = { viewModel.isSplitMenuOpen.value = it },
            onAttachSheetToggle = { viewModel.isAttachSheetOpen.value = it },
            onBrushTypeChange = { viewModel.brushType.value = it },
            onBrushColorChange = { col, hex ->
              viewModel.brushColor.value = col
              viewModel.brushColorHex.value = hex
            },
            onBrushOpacityChange = { viewModel.brushOpacity.value = it },
            onBrushSizeChange = { viewModel.brushSize.value = it },
            onAddStroke = { stroke ->
              viewModel.addStroke(stroke)
              if (autoSaveEnabled) {
                viewModel.saveCurrentProject()
              }
            },
            onPanChanged = { offset -> viewModel.canvasPanOffset.value = offset },
            onToggleLayerVisibility = { id -> viewModel.toggleLayerVisibility(id) },
            onDeleteProject = { viewModel.deleteCurrentActiveProject() },
          )
        }
      }
    }
  }
}

private fun BagViewModel.viewModelScopeLaunchShare() {
  // Trigger share message in UI
  this.selectColorForStudio(this.brushColorHex.value, this.brushColor.value)
}

// Retained for test suite compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme { Greeting("Android") }
}
