package com.example.camera

import android.graphics.*
import android.graphics.drawable.Drawable
import kotlin.math.min

class CircleDrawable : Drawable() {

    var strokeWidth = 5F
        set(value) {
            paintStroke.strokeWidth = value
            this.invalidateSelf()
        }

    var strokeColor = Color.WHITE
        set(value) {
            paintStroke.color = value
            invalidateSelf()
        }

    var color = Color.RED
        set(value) {
            paintFill.color = value
            invalidateSelf()
        }

    private val paintFill = Paint().also {
        it.isAntiAlias = true
        it.isDither = true
        it.style = Paint.Style.FILL
        it.color = color
    }

    private val paintStroke = Paint().also {
        it.isDither = true
        it.style =  Paint.Style.STROKE
        it.strokeWidth = strokeWidth
        it.color = strokeColor
        it.strokeJoin = Paint.Join.ROUND
        it.strokeCap = Paint.Cap.ROUND
    }

    override fun draw(canvas: Canvas) {
        // Get the drawable's bounds
        val width: Int = bounds.width()
        val height: Int = bounds.height()
        val radius: Float = min(width, height).toFloat() / 2f - strokeWidth

        // Draw a red circle in the center
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paintFill)
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paintStroke)
    }

    override fun setAlpha(alpha: Int) {
        paintFill.alpha = alpha
    }

    override fun setColorFilter(p0: ColorFilter?) {
        paintFill.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }


}