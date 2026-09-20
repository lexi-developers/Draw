package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BagProject
import com.example.ui.components.ExpressiveDivider
import com.example.ui.components.ExpressiveNavigationBar
import com.example.ui.components.ExpressiveProjectCarousel
import com.example.ui.components.ExpressiveSearchBar
import com.example.ui.components.ExpressiveTonalFab

/**
 * 屏幕 2：
 * - 上部靠左放置文本“歡迎回來”（57sp）。
 * - 上部居中放置文本“繼續完成你的專案吧！”（28sp）。
 * - 上部放置占位文字为“搜尋”的搜索栏（右端有 mic 图标）。
 * - 中部放置多浏览布局的轮播（8 张卡片，高 180dp，每张卡片圆角 16dp，横向滚动）。
 * - 下部靠右放置 add 图标的色调 FAB。
 * - 下部放置 4 个项目的导航栏（第一项“首頁”为选中状态）。
 */
@Composable
fun Screen2Home(
  recentProjects: List<BagProject>,
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  onSearchClick: () -> Unit,
  onProjectClick: (BagProject) -> Unit,
  onAddProjectClick: () -> Unit,
  onNavigateToTab: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val scrollState = rememberScrollState()

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
      .windowInsetsPadding(WindowInsets.statusBars)
      .testTag("screen_2_home"),
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 80.dp) // space for bottom navigation bar
        .verticalScroll(scrollState),
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      // 上部靠左放置文本“歡迎回來”（57sp）
      Text(
        text = "歡迎回來",
        fontSize = 57.sp,
        lineHeight = 64.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .testTag("text_welcome_back"),
      )

      Spacer(modifier = Modifier.height(12.dp))

      // 上部居中放置文本“繼續完成你的專案吧！”（28sp）
      Text(
        text = "繼續完成你的專案吧！",
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .testTag("text_continue_project"),
      )

      Spacer(modifier = Modifier.height(20.dp))

      // 上部放置占位文字为“搜尋”的搜索栏（右端有 mic 图标）
      ExpressiveSearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearchClick = onSearchClick,
        placeholder = "搜尋",
        modifier = Modifier.padding(horizontal = 16.dp),
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Section Title: 最近作品
      Text(
        text = "近期訂製作品",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp),
      )

      Spacer(modifier = Modifier.height(12.dp))

      // 中部放置多浏览布局的轮播（8 张卡片，高 180dp，每张卡片圆角 16dp，横向滚动）
      ExpressiveProjectCarousel(
        projects = recentProjects,
        onProjectClick = onProjectClick,
        cardCount = 8,
      )

      Spacer(modifier = Modifier.height(24.dp))

      // 分割线
      ExpressiveDivider()

      Spacer(modifier = Modifier.height(16.dp))

      // Luxury Brand highlight banner
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .background(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = MaterialTheme.shapes.extraLarge,
          )
          .padding(20.dp),
      ) {
        Column {
          Text(
            text = "LEE JI-EUN ATELIER 訂製特權",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "李誌恩親選手工頂級皮革",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "全台精品工坊尊榮預約，提供專屬刻字、燙金徽章與超過一萬種義大利植鞣皮色選。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      Spacer(modifier = Modifier.height(32.dp))
    }

    // 下部靠右放置 add 图标的色调 FAB（距屏幕边缘 16dp 悬浮，阴影为 Level 3，上面覆盖在内容上方）
    ExpressiveTonalFab(
      onClick = onAddProjectClick,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 16.dp, bottom = 96.dp), // floats above 80dp nav bar
      contentDescription = "新建專案",
    )

    // 下部放置 4 个项目的导航栏（“首頁”为选中状态）
    ExpressiveNavigationBar(
      selectedIndex = 0,
      onItemSelected = onNavigateToTab,
      modifier = Modifier.align(Alignment.BottomCenter),
    )

    // 上部居中放置标准样式的悬浮工具栏（图标按钮：format_bold、format_italic、format_underlined、attach_file）（部分覆盖在文本之上，绘制在前面）
    Surface(
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(top = 72.dp)
        .height(56.dp)
        .shadow(elevation = 6.dp, shape = CircleShape)
        .testTag("home_top_floating_toolbar"),
      color = MaterialTheme.colorScheme.surfaceContainer,
      shape = CircleShape,
      tonalElevation = 4.dp,
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        IconButton(
          onClick = {},
          modifier = Modifier.size(44.dp).testTag("toolbar_btn_bold"),
        ) {
          Icon(
            imageVector = Icons.Filled.FormatBold,
            contentDescription = "粗體",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        IconButton(
          onClick = {},
          modifier = Modifier.size(44.dp).testTag("toolbar_btn_italic"),
        ) {
          Icon(
            imageVector = Icons.Filled.FormatItalic,
            contentDescription = "斜體",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        IconButton(
          onClick = {},
          modifier = Modifier.size(44.dp).testTag("toolbar_btn_underline"),
        ) {
          Icon(
            imageVector = Icons.Filled.FormatUnderlined,
            contentDescription = "底線",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        IconButton(
          onClick = {},
          modifier = Modifier.size(44.dp).testTag("toolbar_btn_attach"),
        ) {
          Icon(
            imageVector = Icons.Filled.AttachFile,
            contentDescription = "附件",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }
  }
}
