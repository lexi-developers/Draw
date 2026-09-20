package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FavoriteColor
import com.example.ui.components.ExpressiveNavigationBar
import com.example.ui.components.ExpressiveTonalFab
import com.example.util.ColorBlockInfo
import com.example.util.ColorPaletteEngine

/**
 * 屏幕 4：
 * - 上部靠左放置文本“設定”（57sp）（同時包含調色盤萬色庫與喜愛顏色標籤）。
 * - 頁面包含超過一萬種顏色方塊（10,800 種獨特皮色）。
 * - 點擊顏色方塊會跳出新視窗（Dialog），顯示該顏色、色號與愛心按鈕，點擊愛心儲存至調色盤喜愛顏色。
 * - 下部靠右放置 add 图标的色调 FAB。
 * - 下部放置 4 个项目的导航栏（“調色盤”为选中状态）。
 */
@Composable
fun Screen4Palette(
  favoriteColors: List<FavoriteColor>,
  selectedColorBlock: ColorBlockInfo?,
  isColorFavorite: Boolean,
  onColorBlockClick: (ColorBlockInfo) -> Unit,
  onDismissDialog: () -> Unit,
  onToggleFavorite: (ColorBlockInfo) -> Unit,
  onApplyColorToStudio: (String, Color) -> Unit,
  onAddProjectClick: () -> Unit,
  onNavigateToTab: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0 = 萬色皮革庫 (10,000+), 1 = 喜愛顏色收藏
  val gridState = rememberLazyGridState()
  val clipboardManager = LocalClipboardManager.current

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
      .windowInsetsPadding(WindowInsets.statusBars)
      .testTag("screen_4_palette"),
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 80.dp), // space for bottom navigation bar
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      // 上部靠左放置文本“設定”（57sp）
      Text(
        text = "設定",
        fontSize = 57.sp,
        lineHeight = 64.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .testTag("text_palette_title"),
      )

      Text(
        text = "李誌恩 10,800 萬色皮革色庫與喜愛調色盤",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
      )

      // Tab selector: 萬色色庫 (10,000+) / 喜愛的顏色
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier
          .padding(horizontal = 16.dp, vertical = 8.dp)
          .clip(CircleShape),
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = {
            Text(
              "超過一萬種顏色庫 (10,800)",
              style = MaterialTheme.typography.labelLarge,
            )
          },
          modifier = Modifier.testTag("tab_all_colors"),
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = {
            Text(
              "喜愛顏色 (${favoriteColors.size})",
              style = MaterialTheme.typography.labelLarge,
            )
          },
          modifier = Modifier.testTag("tab_favorite_colors"),
        )
      }

      if (selectedTab == 0) {
        // 超過一萬種顏色方塊 (10,800)
        LazyVerticalGrid(
          columns = GridCells.Adaptive(minSize = 36.dp),
          state = gridState,
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp),
          modifier = Modifier
            .fillMaxSize()
            .testTag("palette_color_grid"),
        ) {
          items(
            count = ColorPaletteEngine.TOTAL_COLORS,
            key = { it },
          ) { index ->
            val colorInfo = remember(index) { ColorPaletteEngine.getColorAt(index) }
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colorInfo.color)
                .border(
                  width = 0.5.dp,
                  color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                  shape = RoundedCornerShape(8.dp),
                )
                .clickable { onColorBlockClick(colorInfo) }
                .testTag("color_block_$index"),
            )
          }
        }
      } else {
        // 喜愛的顏色列表
        if (favoriteColors.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(32.dp),
            contentAlignment = Alignment.Center,
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(56.dp),
              )
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = "調色盤尚無喜愛顏色",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
              Text(
                text = "請切換至「萬色庫」點擊顏色方塊並點選愛心儲存",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
              )
            }
          }
        } else {
          LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 80.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize(),
          ) {
            items(favoriteColors.size) { i ->
              val fav = favoriteColors[i]
              val parsedColor = remember(fav.hexCode) {
                try {
                  Color(android.graphics.Color.parseColor(fav.hexCode))
                } catch (_: Exception) {
                  Color(0xFF984061)
                }
              }

              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                  .clip(MaterialTheme.shapes.medium)
                  .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                  .clickable {
                    onApplyColorToStudio(fav.hexCode, parsedColor)
                  }
                  .padding(8.dp),
              ) {
                Box(
                  modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(parsedColor)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = fav.hexCode,
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                  text = fav.name,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 1,
                )
              }
            }
          }
        }
      }
    }

    // 新的視窗（Dialog）：顯示點擊的顏色、色號、愛心按鈕
    if (selectedColorBlock != null) {
      AlertDialog(
        onDismissRequest = onDismissDialog,
        shape = RoundedCornerShape(28.dp), // M3 Expressive Dialog 28dp
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        title = {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "李誌恩皮色詳情",
              style = MaterialTheme.typography.titleLarge,
              color = MaterialTheme.colorScheme.onSurface,
            )
            // 愛心按鈕
            IconButton(
              onClick = { onToggleFavorite(selectedColorBlock) },
              modifier = Modifier.testTag("dialog_favorite_button"),
            ) {
              Icon(
                imageVector = if (isColorFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (isColorFavorite) "已收藏" else "加入調色盤喜愛",
                tint = if (isColorFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp),
              )
            }
          }
        },
        text = {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
          ) {
            // 大尺寸顏色預覽
            Box(
              modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(selectedColorBlock.color)
                .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp)),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 色號與複製
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center,
            ) {
              Text(
                text = "色號：${selectedColorBlock.hex}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
              )
              IconButton(
                onClick = {
                  clipboardManager.setText(AnnotatedString(selectedColorBlock.hex))
                },
                modifier = Modifier.size(36.dp),
              ) {
                Icon(
                  imageVector = Icons.Filled.ContentCopy,
                  contentDescription = "複製色號",
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(18.dp),
                )
              }
            }

            Text(
              text = selectedColorBlock.name,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "RGB(${selectedColorBlock.r}, ${selectedColorBlock.g}, ${selectedColorBlock.b})",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        },
        confirmButton = {
          Button(
            onClick = {
              onApplyColorToStudio(selectedColorBlock.hex, selectedColorBlock.color)
              onDismissDialog()
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.testTag("dialog_apply_color_button"),
          ) {
            Icon(Icons.Filled.FormatPaint, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("套用至畫布工坊")
          }
        },
        dismissButton = {
          OutlinedButton(
            onClick = onDismissDialog,
            shape = CircleShape,
          ) {
            Text("關閉")
          }
        },
      )
    }

    // 下部靠右放置 add 图标的色调 FAB
    ExpressiveTonalFab(
      onClick = onAddProjectClick,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 16.dp, bottom = 96.dp),
      contentDescription = "新建專案",
    )

    // 下部放置 4 个项目的导航栏（“調色盤”为选中状态，索引 2）
    ExpressiveNavigationBar(
      selectedIndex = 2,
      onItemSelected = onNavigateToTab,
      modifier = Modifier.align(Alignment.BottomCenter),
    )
  }
}
