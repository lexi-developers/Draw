package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BagProject
import com.example.ui.components.ExpressiveDivider
import com.example.ui.components.ExpressiveProjectCarousel
import com.example.ui.components.ExpressiveSearchBar

/**
 * 屏幕 6：
 * - 上部，从左到右横向排成一行：文本“搜尋”（57sp）、close 图标的描边图标按钮（放在同一行并垂直居中，不要竖着堆叠或换行，最后的图标按钮向右拉伸占满剩余宽度）。
 * - 上部靠左放置文本“搜尋你的作品”（28sp）。
 * - 上部放置占位文字为“搜尋”的搜索栏（右端有 mic 图标）。
 * - 中部放置多浏览布局的轮播（4 张卡片，高 180dp，每张卡片圆角 16dp，横向滚动）。
 */
@Composable
fun Screen6Search(
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  searchResults: List<BagProject>,
  onCloseClick: () -> Unit,
  onProjectClick: (BagProject) -> Unit,
  modifier: Modifier = Modifier,
  onDeleteProject: ((BagProject) -> Unit)? = null,
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
      .windowInsetsPadding(WindowInsets.statusBars)
      .testTag("screen_6_search"),
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
    ) {
      item {
        Spacer(modifier = Modifier.height(16.dp))

        // 上部，从左到右横向排成一行：
        // 文本“搜尋”（57sp）、close 图标的描边图标按钮（放在同一行并垂直居中，不要竖着堆叠或换行，最后的图标按钮向右拉伸占满剩余宽度）
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = "搜尋",
            fontSize = 57.sp,
            lineHeight = 64.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.testTag("text_search_title"),
          )

          Spacer(modifier = Modifier.width(16.dp))

          // close 图标的描边图标按钮向右拉伸占满剩余宽度
          Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd,
          ) {
            OutlinedIconButton(
              onClick = onCloseClick,
              modifier = Modifier
                .size(48.dp)
                .testTag("close_search_button"),
            ) {
              Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "關閉搜尋",
                tint = MaterialTheme.colorScheme.onSurface,
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 上部靠左放置文本“搜尋你的作品”（28sp）
        Text(
          text = "搜尋你的作品",
          fontSize = 28.sp,
          lineHeight = 36.sp,
          fontWeight = FontWeight.Medium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("text_search_subtitle"),
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 上部放置占位文字为“搜尋”的搜索栏（右端有 mic 图标）
        ExpressiveSearchBar(
          query = searchQuery,
          onQueryChange = onSearchQueryChange,
          onSearchClick = {},
          placeholder = "搜尋",
          modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 中部放置多浏览布局的轮播（4 张卡片，高 180dp，每张卡片圆角 16dp，横向滚动）
        ExpressiveProjectCarousel(
          projects = searchResults,
          onProjectClick = onProjectClick,
          cardCount = 4,
        )

        Spacer(modifier = Modifier.height(24.dp))
        ExpressiveDivider()
        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = if (searchQuery.isBlank()) "推薦李誌恩包款與作品" else "搜尋結果 (${searchResults.size})",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))
      }

      items(searchResults, key = { it.id }) { project ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable { onProjectClick(project) }
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
            modifier = Modifier
              .size(16.dp)
              .clip(CircleShape)
              .background(
                try {
                  Color(android.graphics.Color.parseColor(project.baseColorHex))
                } catch (_: Exception) {
                  MaterialTheme.colorScheme.primary
                },
              ),
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = project.title,
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
              text = "${project.bagType} · ${project.price}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
          if (onDeleteProject != null) {
            IconButton(
              onClick = { onDeleteProject(project) },
              modifier = Modifier
                .size(40.dp)
                .testTag("search_delete_${project.id}"),
            ) {
              Icon(
                imageVector = Icons.Filled.DeleteOutline,
                contentDescription = "刪除作品",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(20.dp),
              )
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(32.dp))
      }
    }
  }
}
