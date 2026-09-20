package com.example.util

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

data class ColorBlockInfo(
  val index: Int,
  val hex: String,
  val color: Color,
  val name: String,
  val r: Int,
  val g: Int,
  val b: Int,
)

object ColorPaletteEngine {
  const val TOTAL_COLORS = 10800 // Over 10,000 unique colors!

  fun getColorAt(index: Int): ColorBlockInfo {
    val safeIndex = index.coerceIn(0, TOTAL_COLORS - 1)
    val hue = ((safeIndex % 360) * 1.0f)
    val satTier = ((safeIndex / 360) % 5)
    val saturation = 0.35f + satTier * 0.14f // 0.35 to 0.91
    val lightTier = (safeIndex / 1800)
    val lightness = 0.22f + lightTier * 0.12f // 0.22 to 0.82

    val (rF, gF, bF) = hslToRgb(hue, saturation, lightness)
    val r = (rF * 255f).roundToInt().coerceIn(0, 255)
    val g = (gF * 255f).roundToInt().coerceIn(0, 255)
    val b = (bF * 255f).roundToInt().coerceIn(0, 255)

    val hex = String.format("#%02X%02X%02X", r, g, b)
    val color = Color(r, g, b)
    val name = getLuxuryColorName(hue, saturation, lightness)

    return ColorBlockInfo(
      index = safeIndex,
      hex = hex,
      color = color,
      name = name,
      r = r,
      g = g,
      b = b,
    )
  }

  private fun getLuxuryColorName(hue: Float, sat: Float, light: Float): String {
    val prefix = when {
      light > 0.7f -> "淡雅"
      light > 0.55f -> "柔光"
      light > 0.4f -> "經典"
      light > 0.3f -> "深邃"
      else -> "暗夜"
    }

    val hueName = when (hue.toInt()) {
      in 0..15 -> "珊瑚紅 Coral"
      in 16..35 -> "蜜桃橙 Peach"
      in 36..55 -> "香檳金 Champagne"
      in 56..80 -> "鵝黃 Primrose"
      in 81..140 -> "薄荷翠 Mint"
      in 141..175 -> "翡翠綠 Emerald"
      in 176..205 -> "湖水青 Cyan"
      in 206..240 -> "鳶尾藍 Iris"
      in 241..270 -> "皇家藍 Royal"
      in 271..300 -> "薰衣草紫 Lavender"
      in 301..330 -> "絲絨紫 Plum"
      in 331..345 -> "玫瑰胭 Rose"
      else -> "緋紅 Crimson"
    }

    val leatherFinish = when {
      sat > 0.75f -> "亮面漆皮"
      sat > 0.55f -> "小牛皮"
      else -> "絲絨麂皮"
    }

    return "$prefix $hueName ($leatherFinish)"
  }

  private fun hslToRgb(h: Float, s: Float, l: Float): Triple<Float, Float, Float> {
    val c = (1f - kotlin.math.abs(2f * l - 1f)) * s
    val x = c * (1f - kotlin.math.abs((h / 60f) % 2f - 1f))
    val m = l - c / 2f

    val (r1, g1, b1) = when {
      h < 60f -> Triple(c, x, 0f)
      h < 120f -> Triple(x, c, 0f)
      h < 180f -> Triple(0f, c, x)
      h < 240f -> Triple(0f, x, c)
      h < 300f -> Triple(x, 0f, c)
      else -> Triple(c, 0f, x)
    }

    return Triple(r1 + m, g1 + m, b1 + m)
  }
}
