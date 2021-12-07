package com.example.camera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.values
import kotlinx.android.synthetic.main.photo_fragment.view.*

class DrawableImageView(context: Context, attrs: AttributeSet) :
    AppCompatImageView(context, attrs) {

    private var colorPen: Int = DEFAULT_COLOR_PEN
    private var sizePen: Float = DEFAULT_SIZE_PEN

    private val actionPoint = PointF()

    private val pens = mutableListOf<Pen>().apply {
        add(Pen(colorPen, sizePen))
    }

    private val stickers = mutableListOf<Sticker>()

    private val scaleGestureListener =
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                Log.d(TAG, "Scale factor: ${detector.scaleFactor}")
                Log.d(TAG, "SpanX: ${detector.currentSpanX}")
                Log.d(TAG, "SpanY: ${detector.currentSpanY}")
                if (stickers.size != 0) {
                    val sticker = stickers.last()
                    sticker.canMove = false

                    val matrix = Matrix()
                    matrix.postScale(
                        detector.scaleFactor * sticker.matrix.values()[Matrix.MSCALE_X],
                        detector.scaleFactor * sticker.matrix.values()[Matrix.MSCALE_Y]
                    )

                    val resizedSticker = Bitmap.createBitmap(
                        sticker.bitmapOrg,
                        0,
                        0,
                        sticker.bitmapOrg.width,
                        sticker.bitmapOrg.height,
                        matrix,
                        true
                    )

                    sticker.bitmap = resizedSticker
                    sticker.matrix = matrix
                }
                invalidate()
                return true
            }
        }

    private val gestureDetector = ScaleGestureDetector(
        context,
        scaleGestureListener
    )

    var isDrawable = false
        set(value) {
            field = value
            if (value) isAddableStickers = false
        }

    var isAddableStickers = false
        set(value) {
            field = value
            if (value) isDrawable = false
        }

    private var bitmap: DrawableItem? = null

    override fun onDraw(canvas: Canvas?) {

        super.onDraw(canvas)
        bitmap = createDrawableItem()
        Log.d(
            TAG,
            "Bitmap: ${bitmap?.width} ${bitmap?.height} ${imageView.width} ${imageView.height}"
        )
        Log.d(
            TAG,
            "Coordinate: ${bitmap?.coordinate?.x} ${bitmap?.coordinate?.y} "
        )
        bitmap?.let { it ->
            val rect = RectF(
                it.coordinate.x,
                it.coordinate.y,
                it.coordinate.x + it.width,
                it.coordinate.y + bitmap!!.height
            )
            canvas?.clipRect(rect)

            Log.d(TAG, "Pens size: ${pens.size}")
            pens.forEach { pen ->
                canvas?.drawPath(pen.path, pen.paint)
            }
            stickers.forEach {
                canvas?.drawBitmap(
                    it.bitmap,
                    it.x,
                    it.y,
                    null
                )
            }

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isAddableStickers) {
            Log.d(TAG, "isAddableStickers")
            gestureDetector.onTouchEvent(event)
            var action = ""
            if (stickers.size == 0) return true
            val sticker = stickers.last()
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (sticker.x < event.x && event.x < sticker.x + sticker.bitmap.width &&
                        sticker.y < event.y && event.y < sticker.y + sticker.bitmap.height
                    ) {
                        Log.d(TAG, "clickSticker")
                        actionPoint.x = event.x - sticker.x
                        actionPoint.y = event.y - sticker.y
                        sticker.canMove = true
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    action = "ACTION_MOVE"

                    if (sticker.canMove) {
                        sticker.x = event.x - actionPoint.x
                        sticker.y = event.y - actionPoint.y
                    }
                }
                MotionEvent.ACTION_UP -> {
                    action = "ACTION_UP"
                    sticker.canMove = false
                }
            }
            Log.d(TAG, "Action: $action X: ${actionPoint.x} Y: ${actionPoint.y}")
        }
        if (isDrawable) {
            var action = ""
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    action = "ACTION_DOWN"
                    pens.add(Pen(colorPen, sizePen))
                    pens.last().path.moveTo(event.x, event.y)
                }
                MotionEvent.ACTION_MOVE -> {
                    action = "ACTION_MOVE"
                    pens.last().path.lineTo(event.x, event.y)
                }
                MotionEvent.ACTION_UP -> {
                    action = "ACTION_UP"
                    pens.last().path.lineTo(event.x, event.y)
                }
            }
            Log.d(TAG, "Action: $action X: ${actionPoint.x} Y: ${actionPoint.y}")
        }
        invalidate()
        return true
    }

    private fun createDrawableItem(): DrawableItem? {
        if (drawable == null) return null
        val valuesMatrix = imageMatrix.values()
        val point = PointF(valuesMatrix[Matrix.MTRANS_X], valuesMatrix[Matrix.MTRANS_Y])
        return DrawableItem(
            coordinate = point,
            width = drawable.intrinsicWidth * valuesMatrix[Matrix.MSCALE_X],
            height = drawable.intrinsicHeight * valuesMatrix[Matrix.MSCALE_Y]
        )
    }

    fun setColor(color: Int) {
        colorPen = color
        Log.d(TAG, "Color: $color")
    }

    fun removeDrawable() {
        if (isDrawable && pens.size != 0) {
            pens.removeLast()
        } else if (isAddableStickers && stickers.size != 0) {
            stickers.removeLast()
        }
        invalidate()
    }

    fun addEmoji(idDrawable: Int) {
        if (bitmap == null) return
        val widthCanvas = bitmap!!.width / 2
        val sticker = BitmapFactory.decodeResource(resources, idDrawable)

        val matrix = Matrix()
        if (sticker.width > widthCanvas) {
            val scale = widthCanvas / sticker.width
            matrix.postScale(scale, scale)

        }
        val resizedSticker = Bitmap.createBitmap(
            sticker,
            0,
            0,
            sticker.width,
            sticker.height,
            matrix,
            true
        )

        stickers.add(
            Sticker(
                sticker,
                resizedSticker,
                matrix,
                bitmap!!.coordinate.x,
                bitmap!!.coordinate.y
            )
        )
        Log.d(TAG, "Bitmap: $bitmap")
        invalidate()
    }

    fun clear(){
        stickers.clear()
        pens.clear()
    }

    companion object {
        private const val TAG = "DrawableImageView"
        private const val DEFAULT_SIZE_PEN = 10F
        private const val DEFAULT_COLOR_PEN = Color.WHITE

    }
}