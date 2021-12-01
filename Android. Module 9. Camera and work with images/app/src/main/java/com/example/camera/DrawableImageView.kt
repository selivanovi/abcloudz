package com.example.camera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import kotlinx.android.synthetic.main.activity_main.view.*

class DrawableImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    private val _width = 8f
    private val actionPoint = PointF()
    private val path = Path()

    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.RED
        strokeWidth = _width
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private var bitmap: Bitmap? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        imageView.bitmap?.let {
            val bitmapCanvas = Canvas(it)
            bitmapCanvas.drawPath(path, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var action = ""
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"

                actionPoint.x = event.x
                actionPoint.y = event.y
                path.moveTo(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"

                actionPoint.x = event.x
                actionPoint.y = event.y
                path.lineTo(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                path.lineTo(event.x, event.y)
                actionPoint.x = event.x
                actionPoint.y = event.y
            }
        }

        invalidate()

        Log.d(TAG, "Action: $action X: ${actionPoint.x} Y: ${actionPoint.y}")
        return true
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        bitmap = bm
    }

    companion object {
        private const val TAG = "DrawableImageView"
    }
}