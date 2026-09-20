package com.example.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.ui.viewmodel.CanvasStroke
import org.json.JSONArray
import org.json.JSONObject

/**
 * Serializes and deserializes canvas drawing strokes to and from JSON
 * for persistence in the Room Database.
 */
object StrokeSerializer {

  fun serialize(strokes: List<CanvasStroke>): String {
    if (strokes.isEmpty()) return ""
    try {
      val rootArray = JSONArray()
      for (stroke in strokes) {
        val strokeObj = JSONObject()
        strokeObj.put("color", stroke.color.value.toLong())
        strokeObj.put("strokeWidth", stroke.strokeWidth.toDouble())
        strokeObj.put("alpha", stroke.alpha.toDouble())
        strokeObj.put("brushType", stroke.brushType)

        val pointsArray = JSONArray()
        for (pt in stroke.points) {
          val ptObj = JSONObject()
          ptObj.put("x", pt.x.toDouble())
          ptObj.put("y", pt.y.toDouble())
          pointsArray.put(ptObj)
        }
        strokeObj.put("points", pointsArray)
        rootArray.put(strokeObj)
      }
      return rootArray.toString()
    } catch (e: Exception) {
      e.printStackTrace()
      return ""
    }
  }

  fun deserialize(json: String?): List<CanvasStroke> {
    if (json.isNullOrBlank()) return emptyList()
    val list = mutableListOf<CanvasStroke>()
    try {
      val rootArray = JSONArray(json)
      for (i in 0 until rootArray.length()) {
        val strokeObj = rootArray.getJSONObject(i)
        val colorVal = strokeObj.optLong("color", 0xFF984061)
        val strokeWidth = strokeObj.optDouble("strokeWidth", 8.0).toFloat()
        val alpha = strokeObj.optDouble("alpha", 1.0).toFloat()
        val brushType = strokeObj.optString("brushType", "Leather Paint")

        val pointsArray = strokeObj.optJSONArray("points")
        val points = mutableListOf<Offset>()
        if (pointsArray != null) {
          for (j in 0 until pointsArray.length()) {
            val ptObj = pointsArray.getJSONObject(j)
            val x = ptObj.optDouble("x", 0.0).toFloat()
            val y = ptObj.optDouble("y", 0.0).toFloat()
            points.add(Offset(x, y))
          }
        }
        list.add(
          CanvasStroke(
            points = points,
            color = Color(colorVal.toULong()),
            strokeWidth = strokeWidth,
            alpha = alpha,
            brushType = brushType,
          )
        )
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return list
  }
}
