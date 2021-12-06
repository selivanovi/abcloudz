package com.example.camera

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.values
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.photo_fragment.view.*

class DrawableImageView(context: Context, attrs: AttributeSet) :
    AppCompatImageView(context, attrs) {

    private var colorPen: Int = DEFAULT_COLOR_PEN
    private var sizePen: Float = DEFAULT_SIZE_PEN

    private val actionPoint = PointF()

    private val pens = mutableListOf<Pen>().apply {
        add(Pen(colorPen, sizePen))
    }

    private val stickers = mutableListOf<Bitmap>()

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

    private var bitmap: DrawableItem? = null

    override fun onDraw(canvas: Canvas?) {
        Log.d(
            TAG,
            "Bitmap: ${bitmap?.width} ${bitmap?.height} ${imageView.width} ${imageView.height}"
        )
        super.onDraw(canvas)
        bitmap = createDrawableItem()
        bitmap?.let { it ->
            val rect = RectF(
                it.coordinate.x,
                it.coordinate.y,
                it.coordinate.x + it.width,
                it.coordinate.y + bitmap!!.height
            )
            canvas?.clipRect(rect)
            if (isDrawable) {
                Log.d(TAG, "Pens size: ${pens.size}")
                pens.forEach { pen ->
                    canvas?.drawPath(pen.path, pen.paint)
                }
                stickers.forEach {
                    canvas?.drawBitmap(it, 0F, 0F, null)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isDrawable) return false
        var action = ""
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"

                actionPoint.x = event.x
                actionPoint.y = event.y
                pens.add(Pen(colorPen, sizePen))
                pens.last().path.moveTo(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"

                actionPoint.x = event.x
                actionPoint.y = event.y
                pens.last().path.lineTo(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                actionPoint.x = event.x
                actionPoint.y = event.y
                pens.last().path.lineTo(event.x, event.y)
            }
        }

        invalidate()

        Log.d(TAG, "Action: $action X: ${actionPoint.x} Y: ${actionPoint.y}")
        return true
    }

    fun createDrawableItem(): DrawableItem? {
        if(drawable == null) return null
        val valuesMatrix = imageMatrix.values()
        val point = PointF(valuesMatrix[Matrix.MTRANS_X], valuesMatrix[Matrix.MTRANS_Y])
        return DrawableItem(
            coordinate = point,
            width =  drawable.intrinsicWidth * valuesMatrix[Matrix.MSCALE_X],
            height = drawable.intrinsicHeight * valuesMatrix[Matrix.MSCALE_Y]
        )
    }

    fun getBitmap(): Drawable {
        return imageView.drawable
    }

    fun setColor(color: Int) {
        colorPen = color
        Log.d(TAG, "Color: $color")
    }

    fun removeDrawable(): Boolean {
        return if (pens.size != 0) {
            pens.removeLast()
            invalidate()
            true
        } else
            false
    }

    fun addEmoji(idDrawable: Int) {
        val bitmap = BitmapFactory.decodeResource(resources, idDrawable)
        stickers.add(bitmap)
        Log.d(TAG, "Bitmap: $bitmap")
        invalidate()
    }

    companion object {
        private const val TAG = "DrawableImageView"
        private const val DEFAULT_SIZE_PEN = 10F
        private const val DEFAULT_COLOR_PEN = Color.WHITE

    }
}