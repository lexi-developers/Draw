package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BagProject
import com.example.data.model.FavoriteColor
import com.example.ui.components.ExpressiveFloatingToolbar
import com.example.ui.components.StudioTopBar
import com.example.ui.components.rememberDrawableResId
import com.example.ui.viewmodel.CanvasStroke
import com.example.ui.viewmodel.StudioLayer
import kotlin.math.roundToInt

/**
 * 屏幕 7：創作介面，用於創作作品。
 * - 上部：從左到右橫向排成一行：
 *   “儲存”（帶 check 圖標）的填充拆分按鈕（右側為帶向下箭頭的選單段，展開不儲存直接退出、分享）、
 *   file_copy 圖標的填充圖標按鈕（向右拉伸占滿剩餘寬度，點擊跳出當前圖層選單）。
 * - 下部居中放置標準樣式的懸浮工具欄（back_hand、edit、attach_file、add_circle、arrow_back_ios、arrow_forward_ios）。
 * - 畫筆工具點第二次跳出卡片選單：畫筆種類、調色盤（包含喜愛顏色）、不透明度。
 * - 新增附件點了出現拍照、文件、上傳圖片。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen7Studio(
  activeProject: BagProject?,
  strokes: List<CanvasStroke>,
  favoriteColors: List<FavoriteColor>,
  activeTool: Int, // 0 = Move, 1 = Brush
  isBrushMenuOpen: Boolean,
  brushType: String,
  brushColor: Color,
  brushOpacity: Float,
  brushSize: Float,
  canvasPanOffset: Offset,
  layers: List<StudioLayer>,
  isLayersMenuOpen: Boolean,
  isSplitMenuOpen: Boolean,
  isAttachSheetOpen: Boolean,
  onToolSelect: (Int) -> Unit,
  onUndo: () -> Unit,
  onRedo: () -> Unit,
  onSaveClick: () -> Unit,
  onCloseWithoutSaving: () -> Unit,
  onShare: () -> Unit,
  onLayersToggle: (Boolean) -> Unit,
  onSplitMenuToggle: (Boolean) -> Unit,
  onAttachSheetToggle: (Boolean) -> Unit,
  onBrushTypeChange: (String) -> Unit,
  onBrushColorChange: (Color, String) -> Unit,
  onBrushOpacityChange: (Float) -> Unit,
  onBrushSizeChange: (Float) -> Unit,
  onAddStroke: (CanvasStroke) -> Unit,
  onPanChanged: (Offset) -> Unit,
  onToggleLayerVisibility: (Int) -> Unit,
  modifier: Modifier = Modifier,
  onDeleteProject: (() -> Unit)? = null,
) {
  val context = LocalContext.current
  val resId = rememberDrawableResId(context, activeProject?.previewImageRes ?: "iu_coral_tote")
  val currentStrokePoints = remember { mutableStateListOf<Offset>() }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
      .windowInsetsPadding(WindowInsets.statusBars)
      .testTag("screen_7_studio"),
  ) {
    // 頂部導航欄（SplitButton + file_copy layers button）
    StudioTopBar(
      onSaveClick = onSaveClick,
      isMenuOpen = isSplitMenuOpen,
      onMenuToggle = onSplitMenuToggle,
      onCloseWithoutSaving = onCloseWithoutSaving,
      onShare = onShare,
      onLayersClick = { onLayersToggle(!isLayersMenuOpen) },
      onDeleteProject = onDeleteProject,
      modifier = Modifier.align(Alignment.TopCenter),
    )

    // 中部核心畫布區（支援平移與手繪）
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(top = 72.dp, bottom = 96.dp)
        .offset {
          IntOffset(
            canvasPanOffset.x.roundToInt(),
            canvasPanOffset.y.roundToInt(),
          )
        }
        .pointerInput(activeTool) {
          if (activeTool == 0) {
            // 移動畫布工具
            detectDragGestures { change, dragAmount ->
              change.consume()
              onPanChanged(canvasPanOffset + dragAmount)
            }
          } else {
            // 畫筆工具（繪圖）
            detectDragGestures(
              onDragStart = { offset ->
                currentStrokePoints.clear()
                currentStrokePoints.add(offset)
              },
              onDrag = { change, _ ->
                change.consume()
                currentStrokePoints.add(change.position)
              },
              onDragEnd = {
                if (currentStrokePoints.isNotEmpty()) {
                  val newStroke = CanvasStroke(
                    points = currentStrokePoints.toList(),
                    color = brushColor,
                    strokeWidth = brushSize,
                    alpha = brushOpacity,
                    brushType = brushType,
                  )
                  onAddStroke(newStroke)
                  currentStrokePoints.clear()
                }
              },
              onDragCancel = {
                currentStrokePoints.clear()
              },
            )
          }
        },
      contentAlignment = Alignment.Center,
    ) {
      // 底層包包版型與皮革基底
      Box(
        modifier = Modifier
          .size(width = 340.dp, height = 340.dp)
          .clip(RoundedCornerShape(28.dp))
          .background(MaterialTheme.colorScheme.surfaceContainerHigh)
          .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(28.dp)),
        contentAlignment = Alignment.Center,
      ) {
        // Base leather layer (controlled by layer 1 visibility)
        if (layers.find { it.id == 1 }?.isVisible != false) {
          Image(
            painter = painterResource(id = resId),
            contentDescription = "包包底圖",
            contentScale = ContentScale.Fit,
            modifier = Modifier
              .fillMaxSize()
              .padding(16.dp),
          )
        }

        // 手繪與車線圖層（controlled by layer 3 visibility）
        if (layers.find { it.id == 3 }?.isVisible != false) {
          Canvas(modifier = Modifier.fillMaxSize()) {
            // 繪製歷史筆觸
            for (stroke in strokes) {
              if (stroke.points.size > 1) {
                val path = Path().apply {
                  moveTo(stroke.points.first().x, stroke.points.first().y)
                  for (pt in stroke.points.drop(1)) {
                    lineTo(pt.x, pt.y)
                  }
                }
                drawPath(
                  path = path,
                  color = stroke.color.copy(alpha = stroke.alpha),
                  style = Stroke(
                    width = stroke.strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                  ),
                )
              }
            }

            // 繪製當前拖曳中的筆觸
            if (currentStrokePoints.size > 1) {
              val currentPath = Path().apply {
                moveTo(currentStrokePoints.first().x, currentStrokePoints.first().y)
                for (pt in currentStrokePoints.drop(1)) {
                  lineTo(pt.x, pt.y)
                }
              }
              drawPath(
                path = currentPath,
                color = brushColor.copy(alpha = brushOpacity),
                style = Stroke(
                  width = brushSize,
                  cap = StrokeCap.Round,
                  join = StrokeJoin.Round,
                ),
              )
            }
          }
        }

        // IU 專屬燙金簽名圖層 (layer 4)
        if (layers.find { it.id == 4 }?.isVisible != false) {
          Box(
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .padding(bottom = 28.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF2C1600).copy(alpha = 0.85f))
              .padding(horizontal = 12.dp, vertical = 4.dp),
          ) {
            Text(
              text = "IU · BESPOKE",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
              ),
              color = Color(0xFFFFD9E2),
            )
          }
        }
      }
    }

    // 畫筆選單卡片（點擊畫筆兩次後彈出）
    if (isBrushMenuOpen) {
      Card(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(bottom = 90.dp, start = 16.dp, end = 16.dp)
          .fillMaxWidth()
          .shadow(12.dp, RoundedCornerShape(20.dp))
          .testTag("brush_settings_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "畫筆與調色盤選單",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
              text = brushType,
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.primary,
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // 畫筆種類選擇
          val brushOptions = listOf("細緻描邊筆", "頂級皮革漆", "柔和標記筆", "珠寶金屬車線")
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(brushOptions) { type ->
              val isSelected = brushType == type
              Box(
                modifier = Modifier
                  .clip(CircleShape)
                  .background(
                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer,
                  )
                  .clickable { onBrushTypeChange(type) }
                  .padding(horizontal = 14.dp, vertical = 6.dp),
              ) {
                Text(
                  text = type,
                  style = MaterialTheme.typography.labelMedium,
                  color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // 調色盤（包含喜愛的顏色與預設皮色）
          Text(
            text = "皮革色彩（含喜愛調色盤）",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Spacer(modifier = Modifier.height(6.dp))

          val defaultColors = listOf(
            "#984061" to Color(0xFF984061),
            "#74575F" to Color(0xFF74575F),
            "#FFD9E2" to Color(0xFFFFD9E2),
            "#F6DDE4" to Color(0xFFF6DDE4),
            "#FFDBCA" to Color(0xFFFFDBCA),
            "#3E001D" to Color(0xFF3E001D),
            "#201A1B" to Color(0xFF201A1B),
          )

          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Favorite colors first
            items(favoriteColors) { fav ->
              val col = try {
                Color(android.graphics.Color.parseColor(fav.hexCode))
              } catch (_: Exception) {
                Color(0xFF984061)
              }
              ColorSwatchCircle(
                color = col,
                isSelected = brushColor == col,
                onClick = { onBrushColorChange(col, fav.hexCode) },
              )
            }
            // Default palette colors
            items(defaultColors) { (hex, col) ->
              ColorSwatchCircle(
                color = col,
                isSelected = brushColor == col,
                onClick = { onBrushColorChange(col, hex) },
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // 不透明度與筆刷粗細滑桿
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "不透明度: ${(brushOpacity * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
              Slider(
                value = brushOpacity,
                onValueChange = onBrushOpacityChange,
                valueRange = 0.1f..1.0f,
                colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary),
              )
            }
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "筆刷尺寸: ${brushSize.roundToInt()}dp",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
              Slider(
                value = brushSize,
                onValueChange = onBrushSizeChange,
                valueRange = 2f..32f,
                colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary),
              )
            }
          }
        }
      }
    }

    // 當前圖層選單（點擊 file_copy 按鈕彈出）
    DropdownMenu(
      expanded = isLayersMenuOpen,
      onDismissRequest = { onLayersToggle(false) },
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
        .testTag("layers_menu"),
    ) {
      Text(
        text = "包包設計圖層管理",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
      )
      for (layer in layers) {
        DropdownMenuItem(
          text = {
            Text(
              text = layer.name,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface,
            )
          },
          leadingIcon = {
            Icon(
              imageVector = if (layer.isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
              contentDescription = if (layer.isVisible) "隱藏圖層" else "顯示圖層",
              tint = if (layer.isVisible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
            )
          },
          onClick = {
            onToggleLayerVisibility(layer.id)
          },
        )
      }
    }

    // 新增附件 BottomSheet（拍照、文件、上傳圖片）
    if (isAttachSheetOpen) {
      ModalBottomSheet(
        onDismissRequest = { onAttachSheetToggle(false) },
        sheetState = rememberModalBottomSheetState(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        ) {
          Text(
            text = "新增專案附件",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
          )
          Spacer(modifier = Modifier.height(16.dp))

          AttachmentOptionItem(
            icon = Icons.Filled.CameraAlt,
            title = "拍照",
            subtitle = "拍攝手繪草圖或皮料樣板",
            onClick = { onAttachSheetToggle(false) },
          )
          AttachmentOptionItem(
            icon = Icons.Filled.Description,
            title = "文件",
            subtitle = "匯入訂製規格書與色票 PDF",
            onClick = { onAttachSheetToggle(false) },
          )
          AttachmentOptionItem(
            icon = Icons.Filled.Image,
            title = "上傳圖片",
            subtitle = "從相簿選取刺繡圖樣或客製標誌",
            onClick = { onAttachSheetToggle(false) },
          )
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }

    // 下部居中放置標準樣式的懸浮工具欄（64dp 高，完全圓角）
    ExpressiveFloatingToolbar(
      activeTool = activeTool,
      onToolSelect = onToolSelect,
      onUndo = onUndo,
      onRedo = onRedo,
      onAttachClick = { onAttachSheetToggle(true) },
      onAddElementClick = {
        // 添加配件與金屬五金提示
        onBrushTypeChange("珠寶金屬車線")
      },
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 16.dp),
    )
  }
}

@Composable
private fun ColorSwatchCircle(
  color: Color,
  isSelected: Boolean,
  onClick: () -> Unit,
) {
  Box(
    modifier = Modifier
      .size(36.dp)
      .clip(CircleShape)
      .background(color)
      .border(
        width = if (isSelected) 3.dp else 1.dp,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        shape = CircleShape,
      )
      .clickable(onClick = onClick),
  )
}

@Composable
private fun AttachmentOptionItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  subtitle: String,
  onClick: () -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .padding(vertical = 12.dp, horizontal = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Box(
      modifier = Modifier
        .size(44.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.primaryContainer),
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(24.dp),
      )
    }
    Spacer(modifier = Modifier.width(16.dp))
    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurface,
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}
