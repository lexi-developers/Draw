package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ripple
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * M3 Expressive shape-morphing Loading Indicator
 * Rotating and morphing between rounded shapes inside a secondaryContainer circular container.
 */
@Composable
fun ExpressiveShapeMorphLoadingIndicator(
  modifier: Modifier = Modifier,
  containerSize: Int = 88,
  indicatorSize: Int = 46,
) {
  val infiniteTransition = rememberInfiniteTransition(label = "morph_loading")

  val rotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(2400, easing = LinearEasing),
      repeatMode = RepeatMode.Restart,
    ),
    label = "rotation",
  )

  val morphProgress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(1600, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse,
    ),
    label = "morph",
  )

  val primaryColor = MaterialTheme.colorScheme.primary
  val secondaryColor = MaterialTheme.colorScheme.secondary

  Box(
    modifier = modifier
      .size(containerSize.dp)
      .shadow(elevation = 6.dp, shape = CircleShape)
      .clip(CircleShape)
      .background(MaterialTheme.colorScheme.secondaryContainer),
    contentAlignment = Alignment.Center,
  ) {
    Canvas(
      modifier = Modifier
        .size(indicatorSize.dp)
        .rotate(rotation)
        .testTag("expressive_loading_indicator"),
    ) {
      val w = size.width
      val h = size.height
      val corner = 8f + morphProgress * (w / 2.5f - 8f)

      val path = Path().apply {
        addRoundRect(
          RoundRect(
            left = 4f,
            top = 4f,
            right = w - 4f,
            bottom = h - 4f,
            cornerRadius = CornerRadius(corner, corner),
          ),
        )
      }

      val drawColor = if (morphProgress > 0.5f) primaryColor else secondaryColor
      drawPath(path = path, color = drawColor, style = Fill)
    }
  }
}

/**
 * Common Expressive Divider
 * 1dp outlineVariant, 16dp horizontal margin.
 */
@Composable
fun ExpressiveDivider(modifier: Modifier = Modifier) {
  HorizontalDivider(
    modifier = modifier.padding(horizontal = 16.dp),
    thickness = 1.dp,
    color = MaterialTheme.colorScheme.outlineVariant,
  )
}

/**
 * 4-Item Expressive Navigation Bar
 * Height 80dp, surfaceContainer background, active item indicated by secondaryContainer capsule (64x32dp),
 * filled icon when active, labelMedium text. Respects system navigation bar insets.
 */
@Composable
fun ExpressiveNavigationBar(
  selectedIndex: Int, // 0 = Home, 1 = Projects, 2 = Palette, 3 = Settings
  onItemSelected: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val items = listOf(
    NavigationItem("首頁", Icons.Filled.Home, Icons.Outlined.Home),
    NavigationItem("專案", Icons.Filled.Folder, Icons.Outlined.Folder),
    NavigationItem("調色盤", Icons.Filled.ColorLens, Icons.Outlined.ColorLens),
    NavigationItem("設定", Icons.Filled.Settings, Icons.Outlined.Settings),
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surfaceContainer)
      .windowInsetsPadding(WindowInsets.navigationBars),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .padding(horizontal = 8.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      items.forEachIndexed { index, item ->
        val isSelected = selectedIndex == index
        val interactionSource = remember { MutableInteractionSource() }

        Box(
          modifier = Modifier
            .weight(1f)
            .clickable(
              interactionSource = interactionSource,
              indication = ripple(bounded = false, radius = 32.dp),
              onClick = { onItemSelected(index) },
            )
            .testTag("nav_item_$index"),
          contentAlignment = Alignment.Center,
        ) {
          androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
          ) {
            Box(
              modifier = Modifier
                .size(width = 64.dp, height = 32.dp)
                .clip(CircleShape)
                .background(
                  if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                ),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.label,
                tint = if (isSelected) {
                  MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                  MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(24.dp),
              )
            }
            Text(
              text = item.label,
              style = MaterialTheme.typography.labelMedium,
              color = if (isSelected) {
                MaterialTheme.colorScheme.onSurface
              } else {
                MaterialTheme.colorScheme.onSurfaceVariant
              },
            )
          }
        }
      }
    }
  }
}

private data class NavigationItem(
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
)

/**
 * Tonal FAB (56dp, rounded 16dp, background primaryContainer, icon primary, Level 3 shadow)
 */
@Composable
fun ExpressiveTonalFab(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  contentDescription: String = "新增專案",
) {
  FloatingActionButton(
    onClick = onClick,
    modifier = modifier
      .size(56.dp)
      .testTag("tonal_fab_add"),
    shape = RoundedCornerShape(16.dp),
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    contentColor = MaterialTheme.colorScheme.primary,
    elevation = FloatingActionButtonDefaults.elevation(
      defaultElevation = 6.dp,
      pressedElevation = 10.dp,
    ),
  ) {
    Icon(
      imageVector = Icons.Filled.Add,
      contentDescription = contentDescription,
      modifier = Modifier.size(28.dp),
    )
  }
}

/**
 * Expressive Search Bar
 * Height 56dp, fully rounded capsule, background surfaceContainerHigh, left search icon, right mic icon.
 */
@Composable
fun ExpressiveSearchBar(
  query: String,
  onQueryChange: (String) -> Unit,
  onSearchClick: () -> Unit,
  onMicClick: () -> Unit = {},
  placeholder: String = "搜尋",
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .height(56.dp)
      .clip(CircleShape)
      .background(MaterialTheme.colorScheme.surfaceContainerHigh)
      .padding(horizontal = 6.dp)
      .testTag("expressive_search_bar"),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    IconButton(
      onClick = onSearchClick,
      modifier = Modifier
        .size(48.dp)
        .testTag("search_icon_button"),
    ) {
      Icon(
        imageVector = Icons.Filled.Search,
        contentDescription = "搜尋",
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    Box(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 8.dp),
      contentAlignment = Alignment.CenterStart,
    ) {
      if (query.isEmpty()) {
        Text(
          text = placeholder,
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      androidx.compose.foundation.text.BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
          color = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier.fillMaxWidth(),
      )
    }

    IconButton(
      onClick = onMicClick,
      modifier = Modifier
        .size(48.dp)
        .testTag("mic_icon_button"),
    ) {
      Icon(
        imageVector = Icons.Filled.Mic,
        contentDescription = "語音輸入",
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}
