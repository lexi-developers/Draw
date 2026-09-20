package com.example.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val CoralLightColorScheme = lightColorScheme(
  primary = CoralPrimary,
  onPrimary = CoralOnPrimary,
  primaryContainer = CoralPrimaryContainer,
  onPrimaryContainer = CoralOnPrimaryContainer,
  secondary = CoralSecondary,
  onSecondary = CoralOnSecondary,
  secondaryContainer = CoralSecondaryContainer,
  onSecondaryContainer = CoralOnSecondaryContainer,
  tertiary = CoralTertiary,
  onTertiary = CoralOnTertiary,
  tertiaryContainer = CoralTertiaryContainer,
  onTertiaryContainer = CoralOnTertiaryContainer,
  background = CoralSurface,
  onBackground = CoralOnSurface,
  surface = CoralSurface,
  onSurface = CoralOnSurface,
  surfaceVariant = CoralSurfaceContainerHigh,
  onSurfaceVariant = CoralOnSurfaceVariant,
  surfaceContainerLowest = CoralSurface,
  surfaceContainerLow = CoralSurfaceContainerLow,
  surfaceContainer = CoralSurfaceContainer,
  surfaceContainerHigh = CoralSurfaceContainerHigh,
  surfaceContainerHighest = CoralSurfaceContainerHighest,
  outline = CoralOutline,
  outlineVariant = CoralOutlineVariant,
  inverseSurface = CoralInverseSurface,
  inverseOnSurface = CoralInverseOnSurface,
  inversePrimary = CoralInversePrimary,
  error = CoralError,
  onError = CoralOnError,
  errorContainer = CoralErrorContainer,
  onErrorContainer = CoralOnErrorContainer,
)

// Expressive M3 Shapes: capsule buttons, 20dp cards, 28dp dialogs & large containers
val ExpressiveShapes = Shapes(
  extraSmall = RoundedCornerShape(8.dp),
  small = RoundedCornerShape(12.dp),
  medium = RoundedCornerShape(16.dp),
  large = RoundedCornerShape(20.dp), // Cards 20dp
  extraLarge = RoundedCornerShape(28.dp), // Dialogs & major container boxes 28dp
)

@Composable
fun MyApplicationTheme(
  // Prompt specifies: vertical phone, light mode only, theme colors must not be overridden
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = CoralLightColorScheme,
    typography = Typography,
    shapes = ExpressiveShapes,
    content = content,
  )
}
