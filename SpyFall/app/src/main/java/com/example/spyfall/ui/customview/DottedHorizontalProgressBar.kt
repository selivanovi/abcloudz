package com.example.spyfall.ui.customview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import com.example.spyfall.R

class DottedHorizontalProgressBar(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var dotStep: Float = 20F
    private var minDotRadius: Float = 3F
    private var maxDotRadius: Float = 5F
    private var dotColor: Int = Color.BLACK
    private var timeOut: Int = 500
    private var dotCount: Int = 3

    private val animators = mutableListOf<Animator>()

    private var primaryValueAnimator: ValueAnimator? = null

    init {
        applyAttributeSet(attributeSet)
        val minRadius = convertDpToPixel(minDotRadius, context)
        val maxRadius = convertDpToPixel(maxDotRadius, context)
        val margin = maxRadius - minRadius
        repeat(dotCount) {
            val dot = View(context)
            val layoutParams = LayoutParams(minRadius * 2, minRadius * 2)
            layoutParams.setMargins(
                margin,
                margin,
                margin,
                margin
            )
            dot.layoutParams = layoutParams
            dot.setBackgroundColor(dotColor)
            dot.setBackgroundResource(R.drawable.dot_icon)
            animators.add(getScaleAnimator(dot))
            this.addView(dot)
        }
        primaryValueAnimator = ValueAnimator.ofInt(0, dotCount)
        primaryValueAnimator?.addUpdateListener {
            if (it.animatedValue != dotCount) {
                animators[it.animatedValue as Int].start()
            }
        }
        primaryValueAnimator?.repeatMode = ValueAnimator.RESTART
        primaryValueAnimator?.repeatCount = ValueAnimator.INFINITE
        primaryValueAnimator?.duration = (timeOut * dotCount).toLong()
        primaryValueAnimator?.interpolator = LinearInterpolator()
    }

    private fun getScaleAnimator(view: View): Animator {
        val minScale = 1F
        val maxScale = maxDotRadius / minDotRadius
        Log.d("Scale", "scale: ${maxDotRadius / minDotRadius}")
        return ValueAnimator.ofFloat(minScale, maxScale).apply {
            addUpdateListener {
                view.scaleX = it.animatedValue as Float
                view.scaleY = it.animatedValue as Float
            }
            duration = timeOut.toLong()
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            interpolator = LinearInterpolator()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        primaryValueAnimator?.start()
    }

    private fun applyAttributeSet(attributeSet: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.DottedHorizontalProgressBar,
            0, 0
        ).apply {
            dotCount = getInteger(R.styleable.DottedHorizontalProgressBar_count, dotCount)
            dotColor = getColor(R.styleable.DottedHorizontalProgressBar_color, dotColor)
            minDotRadius =
                getDimension(R.styleable.DottedHorizontalProgressBar_minDotRadius, minDotRadius)
            maxDotRadius =
                getDimension(R.styleable.DottedHorizontalProgressBar_maxDotRadius, maxDotRadius)
            dotStep = getDimension(R.styleable.DottedHorizontalProgressBar_dotStep, dotStep)
            timeOut = getInteger(R.styleable.DottedHorizontalProgressBar_timeout, timeOut)
        }
    }

    companion object {

        fun convertDpToPixel(dp: Float, context: Context): Int =
            (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }
}
