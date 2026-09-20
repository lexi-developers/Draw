package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ExpressiveShapeMorphLoadingIndicator
import kotlinx.coroutines.delay

/**
 * 屏幕 1：
 * 中部居中放置 M3 Expressive 形状变化的加载指示器。
 * 包含品牌名李誌恩精品包包工坊，并在載入後自動跳轉至屏幕 2。
 */
@Composable
fun Screen1Loading(
  onLoadingComplete: () -> Unit,
  modifier: Modifier = Modifier,
) {
  LaunchedEffect(Unit) {
    delay(1400)
    onLoadingComplete()
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
      .clickable { onLoadingComplete() }
      .testTag("screen_1_loading"),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.padding(24.dp),
    ) {
      Text(
        text = "LEE JI-EUN",
        style = MaterialTheme.typography.labelLarge.copy(
          letterSpacing = 4.sp,
          fontWeight = FontWeight.Bold,
        ),
        color = MaterialTheme.colorScheme.primary,
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "李誌恩精品包包工坊",
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.SemiBold,
        ),
        color = MaterialTheme.colorScheme.onSurface,
      )

      Spacer(modifier = Modifier.height(36.dp))

      // 中部居中放置 M3 Expressive 形状变化的加载指示器
      ExpressiveShapeMorphLoadingIndicator()

      Spacer(modifier = Modifier.height(36.dp))

      Text(
        text = "正在載入專屬訂製工作室...",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
      )
    }

    // 下部靠右放置“按钮”的填充按钮（带 add 图标）
    androidx.compose.material3.Button(
      onClick = onLoadingComplete,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
        .testTag("button_screen1_add"),
      shape = CircleShape,
      colors = androidx.compose.material3.ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
      ),
      contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 24.dp, vertical = 14.dp),
    ) {
      androidx.compose.material3.Icon(
        imageVector = androidx.compose.material.icons.Icons.Filled.Add,
        contentDescription = "新增",
        modifier = Modifier.padding(end = 8.dp),
      )
      Text(
        text = "按鈕",
        style = MaterialTheme.typography.labelLarge,
      )
    }
  }
}
