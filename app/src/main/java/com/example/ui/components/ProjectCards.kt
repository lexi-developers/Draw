package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BagProject

/**
 * Multi-browse horizontal carousel:
 * Cards are 180dp high, 16dp rounded corners, horizontal scroll.
 * First card padding 16dp, card spacing 8dp.
 * Card title placed at bottom of card.
 */
@Composable
fun ExpressiveProjectCarousel(
  projects: List<BagProject>,
  onProjectClick: (BagProject) -> Unit,
  modifier: Modifier = Modifier,
  cardCount: Int = 8,
) {
  val displayList = projects.take(cardCount)

  if (displayList.isEmpty()) {
    // When no projects exist, don't show or show subtle placeholder
    Box(
      modifier = modifier
        .fillMaxWidth()
        .height(180.dp)
        .padding(horizontal = 16.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = "尚無最近作品，點擊下方「+」開始專屬訂製",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  } else {
    LazyRow(
      modifier = modifier
        .fillMaxWidth()
        .height(180.dp)
        .testTag("multi_browse_carousel"),
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      items(displayList, key = { it.id }) { project ->
        ExpressiveCarouselCard(
          project = project,
          onClick = { onProjectClick(project) },
        )
      }
    }
  }
}

@Composable
fun ExpressiveCarouselCard(
  project: BagProject,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val resId = rememberDrawableResId(context, project.previewImageRes)

  Box(
    modifier = modifier
      .width(220.dp)
      .height(180.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(MaterialTheme.colorScheme.surfaceContainerHigh)
      .clickable(onClick = onClick)
      .testTag("carousel_card_${project.id}"),
  ) {
    if (resId != null && resId != 0) {
      Image(
        painter = painterResource(id = resId),
        contentDescription = project.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
      )
    } else {
      // Elegant leather tone placeholder
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.radialGradient(
              colors = listOf(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.surfaceContainerHighest,
              ),
            ),
          ),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Filled.ShoppingBag,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(48.dp),
        )
      }
    }

    // Gradient overlay for bottom title readability
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color.Transparent,
              Color.Black.copy(alpha = 0.2f),
              Color.Black.copy(alpha = 0.75f),
            ),
            startY = 60f,
          ),
        ),
    )

    // Price / Base Color Badge at top right
    Box(
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(8.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
        .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
      Text(
        text = project.price,
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary,
      )
    }

    // Card title at bottom of card
    Column(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .fillMaxWidth()
        .padding(12.dp),
    ) {
      Text(
        text = project.title,
        style = MaterialTheme.typography.titleMedium.copy(
          color = Color.White,
          fontWeight = FontWeight.Bold,
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        text = project.bagType,
        style = MaterialTheme.typography.bodySmall.copy(
          color = Color.White.copy(alpha = 0.85f),
        ),
        maxLines = 1,
      )
    }
  }
}

/**
 * Screen 3 Container Box (Large 384x192dp or Stretch 180x192dp)
 * Background surfaceContainerHigh, rounded 28dp, clicking opens corresponding project.
 */
@Composable
fun ExpressiveProjectContainerBox(
  project: BagProject?,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  placeholderTitle: String = "李誌恩經典訂製工坊",
  onDelete: (() -> Unit)? = null,
) {
  val context = LocalContext.current
  val resId = project?.let { rememberDrawableResId(context, it.previewImageRes) }

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(28.dp))
      .background(MaterialTheme.colorScheme.surfaceContainerHigh)
      .clickable(onClick = onClick)
      .testTag("container_box_${project?.id ?: "empty"}"),
  ) {
    if (resId != null && resId != 0) {
      Image(
        painter = painterResource(id = resId),
        contentDescription = project?.title ?: placeholderTitle,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
      )
    }

    // Subtle dark gradient for high readability
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color.Transparent,
              Color.Black.copy(alpha = 0.35f),
              Color.Black.copy(alpha = 0.8f),
            ),
            startY = 40f,
          ),
        ),
    )

    // Delete Button at top right if onDelete provided and project exists
    if (project != null && onDelete != null) {
      IconButton(
        onClick = onDelete,
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(8.dp)
          .size(40.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.75f))
          .testTag("delete_project_${project.id}"),
      ) {
        Icon(
          imageVector = Icons.Filled.DeleteOutline,
          contentDescription = "刪除作品",
          tint = MaterialTheme.colorScheme.error,
          modifier = Modifier.size(20.dp),
        )
      }
    }

    // Bag Project info on top of container box
    Column(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(20.dp),
    ) {
      if (project != null) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primaryContainer),
          )
          Text(
            text = project.bagType,
            style = MaterialTheme.typography.labelMedium.copy(
              color = MaterialTheme.colorScheme.primaryContainer,
              fontWeight = FontWeight.SemiBold,
            ),
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = project.title,
          style = MaterialTheme.typography.titleLarge.copy(
            color = Color.White,
            fontWeight = FontWeight.Bold,
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = "${project.price} · 點擊進入設計工作台",
          style = MaterialTheme.typography.bodyMedium.copy(
            color = Color.White.copy(alpha = 0.85f),
          ),
        )
      } else {
        Text(
          text = placeholderTitle,
          style = MaterialTheme.typography.titleLarge.copy(
            color = Color.White,
            fontWeight = FontWeight.Bold,
          ),
        )
        Text(
          text = "點擊開啟全新李誌恩包包訂製",
          style = MaterialTheme.typography.bodyMedium.copy(
            color = Color.White.copy(alpha = 0.85f),
          ),
        )
      }
    }
  }
}

fun rememberDrawableResId(context: android.content.Context, name: String): Int {
  return when (name) {
    "iu_coral_tote" -> R.drawable.iu_coral_tote
    "iu_crossbody_bag" -> R.drawable.iu_crossbody_bag
    else -> R.drawable.iu_coral_tote
  }
}
