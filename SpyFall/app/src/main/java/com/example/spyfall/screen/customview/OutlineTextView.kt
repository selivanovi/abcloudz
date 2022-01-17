package com.example.spyfall.screen.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.example.spyfall.R


class OutlineTextView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {

    private val defaultOutlineWidth = 0F
    private var isDrawing: Boolean = false

    private var outlineColor: Int = 0
    private var outlineWidth: Float = 0.toFloat()


   init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.OutlineTextView)
            outlineColor = a.getColor(R.styleable.OutlineTextView_outlineColor, currentTextColor)
            outlineWidth = a.getDimension(R.styleable.OutlineTextView_outlineWidth, defaultOutlineWidth)

            a.recycle()
        } else {
            outlineColor = currentTextColor
            outlineWidth = defaultOutlineWidth
        }
        setOutlineWidth(TypedValue.COMPLEX_UNIT_PX, outlineWidth)
    }

    fun setOutlineColor(color: Int) {
        outlineColor = color
    }


    fun setOutlineWidth(unit: Int, width: Float) {
        outlineWidth = TypedValue.applyDimension(
            unit, width, context.resources.displayMetrics)
    }

    override fun invalidate() {
        if (isDrawing) return
        super.invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        isDrawing = true
        paint.style = Paint.Style.FILL
        super.onDraw(canvas)

        val currentTextColor = currentTextColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = outlineWidth
        setTextColor(outlineColor)
        super.onDraw(canvas)
        setTextColor(currentTextColor)

        isDrawing = false
    }
}