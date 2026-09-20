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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.ui.components.ExpressiveDivider
import com.example.ui.components.ExpressiveNavigationBar
import com.example.ui.components.ExpressiveTonalFab

/**
 * 屏幕 5：
 * - 應用程式的完整設定項目。
 * - 下部靠右放置 add 图标的色调 FAB。
 * - 下部放置 4 个项目的导航栏（“設定”为选中状态，索引 3）。
 */
@Composable
fun Screen5Settings(
  notificationsEnabled: Boolean,
  onNotificationsChange: (Boolean) -> Unit,
  autoSaveEnabled: Boolean,
  onAutoSaveChange: (Boolean) -> Unit,
  gridSnapEnabled: Boolean,
  onGridSnapChange: (Boolean) -> Unit,
  highResExport: Boolean,
  onHighResExportChange: (Boolean) -> Unit,
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
      .testTag("screen_5_settings"),
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 80.dp) // space for navigation bar
        .verticalScroll(scrollState),
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      // 標題：設定（M3 Expressive 大字號）
      Text(
        text = "設定",
        fontSize = 57.sp,
        lineHeight = 64.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .testTag("text_settings_title"),
      )

      Text(
        text = "李誌恩精品包包 · 工坊偏好與會員設定",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
      )

      Spacer(modifier = Modifier.height(16.dp))

      // IU VIP 會員尊榮卡片
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp), // M3 Expressive card 20dp
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            Icon(
              imageVector = Icons.Filled.CardMembership,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(28.dp),
            )
            Column {
              Text(
                text = "IU Atelier 尊榮會員",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
              )
              Text(
                text = "VIP 專屬手工訂製折扣 15% · 免運親送",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 設定分組 1：設計工作台偏好
      Text(
        text = "包包創作工作台",
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp),
      )

      Spacer(modifier = Modifier.height(8.dp))

      SettingsSwitchItem(
        title = "自動儲存設計專案",
        subtitle = "在繪製筆觸或修改皮色時即時自動保存",
        icon = Icons.Filled.Save,
        checked = autoSaveEnabled,
        onCheckedChange = onAutoSaveChange,
      )

      SettingsSwitchItem(
        title = "畫布輔助對齊格線",
        subtitle = "精確定位拉鍊、背帶釦環與 IU 燙金簽名",
        icon = Icons.Filled.Tune,
        checked = gridSnapEnabled,
        onCheckedChange = onGridSnapChange,
      )

      SettingsSwitchItem(
        title = "4K 高解析度渲染預覽",
        subtitle = "開啟頂級皮革顆粒與車縫線細節光影",
        icon = Icons.Filled.Palette,
        checked = highResExport,
        onCheckedChange = onHighResExportChange,
      )

      Spacer(modifier = Modifier.height(16.dp))
      ExpressiveDivider()
      Spacer(modifier = Modifier.height(16.dp))

      // 設定分組 2：精品工坊與訂單
      Text(
        text = "精品工坊與通知",
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp),
      )

      Spacer(modifier = Modifier.height(8.dp))

      SettingsSwitchItem(
        title = "限量包款新品與發售通知",
        subtitle = "第一時間接收李誌恩親選色系包款限量名額",
        icon = Icons.Filled.Notifications,
        checked = notificationsEnabled,
        onCheckedChange = onNotificationsChange,
      )

      Spacer(modifier = Modifier.height(16.dp))
      ExpressiveDivider()
      Spacer(modifier = Modifier.height(16.dp))

      // 關於李誌恩品牌
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Filled.Info,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
              text = "關於 LEE JI-EUN 精品包包",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface,
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "由李誌恩主理的奢華皮件品牌，將古典優雅與現代藝術完美揉合。每款包包均由義大利頂尖工匠手工縫製，支援高達萬種色彩訂製。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "版本 1.0 · Material 3 Expressive 原生架構",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
          )
        }
      }

      Spacer(modifier = Modifier.height(32.dp))
    }

    // 下部靠右放置 add 图标的色调 FAB
    ExpressiveTonalFab(
      onClick = onAddProjectClick,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 16.dp, bottom = 96.dp),
      contentDescription = "新建專案",
    )

    // 下部放置 4 个项目的导航栏（“設定”为选中状态，索引 3）
    ExpressiveNavigationBar(
      selectedIndex = 3,
      onItemSelected = onNavigateToTab,
      modifier = Modifier.align(Alignment.BottomCenter),
    )
  }
}

@Composable
fun SettingsSwitchItem(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Box(
      modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(20.dp),
      )
    }

    Spacer(modifier = Modifier.size(14.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
        color = MaterialTheme.colorScheme.onSurface,
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
        checkedTrackColor = MaterialTheme.colorScheme.primary,
      ),
    )
  }
}
