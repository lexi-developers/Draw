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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BagProject
import com.example.ui.components.ExpressiveNavigationBar
import com.example.ui.components.ExpressiveProjectContainerBox
import com.example.ui.components.ExpressiveTonalFab

/**
 * 屏幕 3：
 * - 上部靠左放置文本“專案”（57sp）。
 * - 上部靠左放置文本“檢視你的所有專案”（28sp）。
 * - 中部放置 384×192dp 的容器框（背景 surfaceContainerHigh，圆角 28dp）。
 * - 中部，从左到右横向排成一行：180×192dp 的容器框、180×192dp 的容器框（放在同一行并垂直居中，不要竖着堆叠或换行，最后的容器框向右拉伸占满剩余宽度）。
 * - 中部放置 384×192dp 的容器框（背景 surfaceContainerHigh，圆角 28dp）。
 * - 下部靠右放置 add 图标的色调 FAB（部分覆盖在容器框之上，绘制在前面）。
 * - 下部放置 4 个项目的导航栏（“專案”为选中状态）。
 */
@Composable
fun Screen3Projects(
  projects: List<BagProject>,
  onProjectClick: (BagProject) -> Unit,
  onAddProjectClick: () -> Unit,
  onNavigateToTab: (Int) -> Unit,
  modifier: Modifier = Modifier,
  onDeleteProject: ((BagProject) -> Unit)? = null,
) {
  val scrollState = rememberScrollState()

  val project1 = projects.getOrNull(0)
  val project2 = projects.getOrNull(1)
  val project3 = projects.getOrNull(2)
  val project4 = projects.getOrNull(3)

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
      .windowInsetsPadding(WindowInsets.statusBars)
      .testTag("screen_3_projects"),
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 80.dp) // Leave space for 80dp bottom navigation bar
        .verticalScroll(scrollState),
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      // 上部靠左放置文本“專案”（57sp）
      Text(
        text = "專案",
        fontSize = 57.sp,
        lineHeight = 64.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .testTag("text_projects_title"),
      )

      Spacer(modifier = Modifier.height(12.dp))

      // 上部靠左放置文本“檢視你的所有專案”（28sp）
      Text(
        text = "檢視你的所有專案",
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .testTag("text_view_all_projects"),
      )

      Spacer(modifier = Modifier.height(20.dp))

      // 中部放置 384×192dp 的容器框（背景 surfaceContainerHigh，圆角 28dp）
      ExpressiveProjectContainerBox(
        project = project1,
        onClick = { project1?.let(onProjectClick) ?: onAddProjectClick() },
        placeholderTitle = "IU 經典珊瑚托特包",
        onDelete = project1?.let { p -> { onDeleteProject?.invoke(p) } },
        modifier = Modifier
          .fillMaxWidth()
          .height(192.dp)
          .padding(horizontal = 16.dp),
      )

      Spacer(modifier = Modifier.height(16.dp))

      // 中部，从左到右横向排成一行：
      // 180×192dp 的容器框、180×192dp 的容器框（放在同一行并垂直居中，不要竖着堆叠或换行，最后的容器框向右拉伸占满剩余宽度）
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(192.dp)
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        // 第一個容器框 180x192dp
        ExpressiveProjectContainerBox(
          project = project2,
          onClick = { project2?.let(onProjectClick) ?: onAddProjectClick() },
          placeholderTitle = "玫瑰晨露斜背包",
          onDelete = project2?.let { p -> { onDeleteProject?.invoke(p) } },
          modifier = Modifier
            .width(180.dp)
            .height(192.dp),
        )

        // 第二個容器框向右拉伸占满剩余宽度（基礎寬度約 180dp）
        ExpressiveProjectContainerBox(
          project = project3,
          onClick = { project3?.let(onProjectClick) ?: onAddProjectClick() },
          placeholderTitle = "緋紅晚宴珠寶包",
          onDelete = project3?.let { p -> { onDeleteProject?.invoke(p) } },
          modifier = Modifier
            .weight(1f)
            .height(192.dp),
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 中部放置 384×192dp 的容器框（背景 surfaceContainerHigh，圆角 28dp）
      // 下部靠右放置 add 图标的色调 FAB（部分覆盖在容器框之上，绘制在前面）
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(192.dp)
          .padding(horizontal = 16.dp),
      ) {
        ExpressiveProjectContainerBox(
          project = project4,
          onClick = { project4?.let(onProjectClick) ?: onAddProjectClick() },
          placeholderTitle = "蜜桃法式馬鞍包",
          onDelete = project4?.let { p -> { onDeleteProject?.invoke(p) } },
          modifier = Modifier.fillMaxSize(),
        )

        // FAB 部分覆盖在容器框之上，绘制在前面
        ExpressiveTonalFab(
          onClick = onAddProjectClick,
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 12.dp, bottom = 12.dp),
          contentDescription = "新建作品",
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }

    // 下部放置 4 个项目的导航栏（“專案”为选中状态，索引 1）
    ExpressiveNavigationBar(
      selectedIndex = 1,
      onItemSelected = onNavigateToTab,
      modifier = Modifier.align(Alignment.BottomCenter),
    )
  }
}
