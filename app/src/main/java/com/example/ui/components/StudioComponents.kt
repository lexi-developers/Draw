package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * M3 Expressive SplitButton:
 * Left segment: Primary action "儲存" (with check icon)
 * Right segment: Arrow that opens DropdownMenu (close without saving, share)
 * Spacing between segments 2dp, outer corner fully rounded, adjacent inner corner 8dp.
 * When menu open, arrow rotates.
 */
@Composable
fun ExpressiveSplitButton(
  onSaveClick: () -> Unit,
  isMenuOpen: Boolean,
  onMenuToggle: (Boolean) -> Unit,
  onCloseWithoutSaving: () -> Unit,
  onShare: () -> Unit,
  modifier: Modifier = Modifier,
  heightDp: Int = 48,
  onDeleteProject: (() -> Unit)? = null,
) {
  val arrowRotation by animateFloatAsState(
    targetValue = if (isMenuOpen) 180f else 0f,
    animationSpec = tween(durationMillis = 250),
    label = "arrow_rot",
  )

  Box(modifier = modifier) {
    Row(
      modifier = Modifier
        .height(heightDp.dp)
        .testTag("expressive_split_button"),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
      // Left primary action: "儲存" with check icon
      Box(
        modifier = Modifier
          .clip(
            RoundedCornerShape(
              topStart = (heightDp / 2).dp,
              bottomStart = (heightDp / 2).dp,
              topEnd = 8.dp,
              bottomEnd = 8.dp,
            ),
          )
          .background(MaterialTheme.colorScheme.primary)
          .clickable { onSaveClick() }
          .padding(horizontal = 16.dp, vertical = 8.dp)
          .testTag("split_button_save"),
        contentAlignment = Alignment.Center,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "儲存",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(20.dp),
          )
          Text(
            text = "儲存",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onPrimary,
          )
        }
      }

      // Right menu segment with arrow
      Box(
        modifier = Modifier
          .size(width = 44.dp, height = heightDp.dp)
          .clip(
            RoundedCornerShape(
              topStart = 8.dp,
              bottomStart = 8.dp,
              topEnd = (heightDp / 2).dp,
              bottomEnd = (heightDp / 2).dp,
            ),
          )
          .background(MaterialTheme.colorScheme.primary)
          .clickable { onMenuToggle(!isMenuOpen) }
          .testTag("split_button_dropdown"),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Filled.KeyboardArrowDown,
          contentDescription = "展開選項",
          tint = MaterialTheme.colorScheme.onPrimary,
          modifier = Modifier
            .size(20.dp)
            .rotate(arrowRotation),
        )
      }
    }

    // Dropdown menu for split button (unfolds with items)
    DropdownMenu(
      expanded = isMenuOpen,
      onDismissRequest = { onMenuToggle(false) },
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
        .testTag("split_button_menu"),
    ) {
      DropdownMenuItem(
        text = { Text("不儲存直接退出", color = MaterialTheme.colorScheme.error) },
        leadingIcon = {
          Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "不儲存直接退出",
            tint = MaterialTheme.colorScheme.error,
          )
        },
        onClick = {
          onMenuToggle(false)
          onCloseWithoutSaving()
        },
        modifier = Modifier.testTag("menu_item_exit"),
      )
      DropdownMenuItem(
        text = { Text("分享作品", color = MaterialTheme.colorScheme.onSurface) },
        leadingIcon = {
          Icon(
            imageVector = Icons.Filled.Share,
            contentDescription = "分享",
            tint = MaterialTheme.colorScheme.primary,
          )
        },
        onClick = {
          onMenuToggle(false)
          onShare()
        },
        modifier = Modifier.testTag("menu_item_share"),
      )
      if (onDeleteProject != null) {
        DropdownMenuItem(
          text = { Text("刪除此作品", color = MaterialTheme.colorScheme.error) },
          leadingIcon = {
            Icon(
              imageVector = Icons.Filled.DeleteOutline,
              contentDescription = "刪除此作品",
              tint = MaterialTheme.colorScheme.error,
            )
          },
          onClick = {
            onMenuToggle(false)
            onDeleteProject()
          },
          modifier = Modifier.testTag("menu_item_delete"),
        )
      }
    }
  }
}

/**
 * Top Row for Screen 7:
 * "儲存" SplitButton on left, and file_copy filled icon button stretching to fill remaining width.
 */
@Composable
fun StudioTopBar(
  onSaveClick: () -> Unit,
  isMenuOpen: Boolean,
  onMenuToggle: (Boolean) -> Unit,
  onCloseWithoutSaving: () -> Unit,
  onShare: () -> Unit,
  onLayersClick: () -> Unit,
  modifier: Modifier = Modifier,
  onDeleteProject: (() -> Unit)? = null,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp)
      .testTag("studio_top_bar"),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    ExpressiveSplitButton(
      onSaveClick = onSaveClick,
      isMenuOpen = isMenuOpen,
      onMenuToggle = onMenuToggle,
      onCloseWithoutSaving = onCloseWithoutSaving,
      onShare = onShare,
      onDeleteProject = onDeleteProject,
    )

    // file_copy filled icon button that stretches to fill remaining width
    FilledIconButton(
      onClick = onLayersClick,
      modifier = Modifier
        .weight(1f)
        .height(48.dp)
        .testTag("file_copy_layers_button"),
      shape = CircleShape,
      colors = IconButtonDefaults.filledIconButtonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      ),
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Icon(
          imageVector = Icons.Filled.FileCopy,
          contentDescription = "圖層管理",
          modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "圖層",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.SemiBold,
        )
      }
    }
  }
}

/**
 * M3 Expressive Horizontal Floating Toolbar
 * Height 64dp, fully rounded capsule, hovering 16dp above bottom.
 * Buttons (48dp):
 * 1. back_hand (move canvas)
 * 2. edit (brush tool, click opens card menu)
 * 3. arrow_back_ios (undo)
 * 4. arrow_forward_ios (redo)
 * 5. attach_file (take photo / document / upload image)
 * 6. add_circle (add decorative element / charm)
 */
@Composable
fun ExpressiveFloatingToolbar(
  activeTool: Int, // 0 = Pan, 1 = Brush
  onToolSelect: (Int) -> Unit,
  onUndo: () -> Unit,
  onRedo: () -> Unit,
  onAttachClick: () -> Unit,
  onAddElementClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier
      .height(64.dp)
      .shadow(elevation = 8.dp, shape = CircleShape)
      .clip(CircleShape)
      .testTag("expressive_floating_toolbar"),
    color = MaterialTheme.colorScheme.surfaceContainer,
    shape = CircleShape,
    tonalElevation = 6.dp,
  ) {
    Row(
      modifier = Modifier
        .padding(horizontal = 8.dp)
        .fillMaxHeight(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      // 1. back_hand (Move canvas)
      IconButton(
        onClick = { onToolSelect(0) },
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(
            if (activeTool == 0) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
          )
          .testTag("tool_pan"),
      ) {
        Icon(
          imageVector = Icons.Filled.PanTool,
          contentDescription = "移動畫布",
          tint = if (activeTool == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(22.dp),
        )
      }

      // 2. edit (Brush)
      IconButton(
        onClick = { onToolSelect(1) },
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(
            if (activeTool == 1) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
          )
          .testTag("tool_brush"),
      ) {
        Icon(
          imageVector = Icons.Filled.Edit,
          contentDescription = "皮革畫筆",
          tint = if (activeTool == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(22.dp),
        )
      }

      // 3. arrow_back_ios (Undo)
      IconButton(
        onClick = onUndo,
        modifier = Modifier
          .size(48.dp)
          .testTag("tool_undo"),
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
          contentDescription = "返回上一步",
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(20.dp),
        )
      }

      // 4. arrow_forward_ios (Redo)
      IconButton(
        onClick = onRedo,
        modifier = Modifier
          .size(48.dp)
          .testTag("tool_redo"),
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
          contentDescription = "重做",
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(20.dp),
        )
      }

      // 5. attach_file (Take photo / Document / Upload image)
      IconButton(
        onClick = onAttachClick,
        modifier = Modifier
          .size(48.dp)
          .testTag("tool_attach"),
      ) {
        Icon(
          imageVector = Icons.Filled.AttachFile,
          contentDescription = "新增附件",
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(22.dp),
        )
      }

      // 6. add_circle (Add element / Charm)
      IconButton(
        onClick = onAddElementClick,
        modifier = Modifier
          .size(48.dp)
          .testTag("tool_add_element"),
      ) {
        Icon(
          imageVector = Icons.Filled.AddCircle,
          contentDescription = "添加配件與飾釦",
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(24.dp),
        )
      }
    }
  }
}
