package com.movieapp


import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import com.facebook.drawee.drawable.ProgressBarDrawable

/**
 * Creates custom progress bar.
 */
class CustomProgressBar : ProgressBarDrawable() {
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mLevel = 0
    private val maxLevel = 10000


    override fun onLevelChange(level: Int): Boolean {
        mLevel = level
        invalidateSelf()
        return true
    }

    override fun draw(canvas: Canvas) {
        Log.v(TAG, String.format("draw() :: level = %d", mLevel))

        if (hideWhenZero && mLevel == 0) {
            return
        }
        drawBar(canvas, maxLevel, backgroundColor)
        drawBar(canvas, mLevel, color)
    }

    /**
     * Draw progress bar to show progress.
     * @param canvas view on which progress displayed
     * @param level level of progress
     * @param color name of color used to show progress
     */
    private fun drawBar(canvas: Canvas, level: Int, color: Int) {
        val bounds = bounds
        val rectF = RectF((bounds.right * .3).toFloat(), (bounds.bottom * .3).toFloat(),
                (bounds.right * .6).toFloat(), (bounds.bottom * .6).toFloat())
        mPaint.color = color
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 16f
        if (level != 0)
            canvas.drawArc(rectF, 0f, (level * 360 / maxLevel).toFloat(), false, mPaint)
    }

    companion object {
        private val TAG = "CustomProgressBar"
    }
}
