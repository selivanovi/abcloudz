package com.example.camera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.values
import kotlinx.android.synthetic.main.activity_main.view.*

class DrawableImageView(context: Context, attrs: AttributeSet) :
    AppCompatImageView(context, attrs) {

    private val _width = 8f
    private val actionPoint = PointF()
    private val pathLine = Path()

    private val scaleGestureListener =
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                return super.onScale(detector)
            }
        }

    private val gestureDetector = ScaleGestureDetector(
        context,
        scaleGestureListener
    )

    var isDrawable = false
    val isAddableStickers = false

    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.RED
        strokeWidth = _width
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private var bitmap: DrawableItem? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isDrawable) {
            canvas?.drawPath(pathLine, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        var action = ""
        val bitmap = bitmap!!
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"

                actionPoint.x = event.x
                actionPoint.y = event.y

                if (actionPoint.x > bitmap.coordinate.x && actionPoint.x < bitmap.coordinate.x + height &&
                    actionPoint.y > bitmap.coordinate.y && actionPoint.y < bitmap.coordinate.y + width
                )
                    pathLine.moveTo(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"

                actionPoint.x = event.x
                actionPoint.y = event.y
                if (actionPoint.x > bitmap.coordinate.x && actionPoint.x < bitmap.coordinate.x + height &&
                    actionPoint.y > bitmap.coordinate.y && actionPoint.y < bitmap.coordinate.y + width
                )
                    pathLine.lineTo(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                actionPoint.x = event.x
                actionPoint.y = event.y
                if (actionPoint.x > bitmap.coordinate.x && actionPoint.x < bitmap.coordinate.x + height &&
                    actionPoint.y > bitmap.coordinate.y && actionPoint.y < bitmap.coordinate.y + width
                )
                    pathLine.lineTo(event.x, event.y)


            }
        }

        invalidate()

        Log.d(TAG, "Action: $action X: ${actionPoint.x} Y: ${actionPoint.y}")
        return true
    }

    override fun setImageUri(uri: Uri?) {
        val valuesMatrix = imageMatrix.values()
        val point = PointF(valuesMatrix[Matrix.MTRANS_X], valuesMatrix[Matrix.MTRANS_Y])
        bitmap = DrawableItem(
            coordinate = point,
            width = drawable.intrinsicWidth.toFloat(),
            height = drawable.intrinsicHeight.toFloat()
        )
        Log.d(TAG, "Bitmap: $bitmap")
        super.setImageUri(uri)
    }

    companion object {
        private const val TAG = "DrawableImageView"
    }
}