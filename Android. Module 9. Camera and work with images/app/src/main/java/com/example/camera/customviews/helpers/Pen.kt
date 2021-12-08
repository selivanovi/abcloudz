package com.example.camera.customviews.helpers

import android.graphics.Paint
import android.graphics.Path

class Pen(colorPen: Int, sizePen: Float) {

    val path: Path = Path()

    val paint: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = colorPen
        strokeWidth = sizePen
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
}