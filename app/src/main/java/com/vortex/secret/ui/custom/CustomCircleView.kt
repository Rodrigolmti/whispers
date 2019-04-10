package com.vortex.secret.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.vortex.secret.BuildConfig
import com.vortex.secret.R

class CustomCircleView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint: Paint = Paint()
    private var color: Int? = null

    init {
        if (BuildConfig.DEBUG) {
            color = R.color.sea_buckthorn
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        color?.let {

            val radius = 60F
            paint.style = Paint.Style.FILL
            paint.color = Color.WHITE
            canvas.drawPaint(paint)

            paint.color = ContextCompat.getColor(context, it)

            canvas.drawCircle(width / 2F, height / 2f, radius, paint)
        }
    }

    fun updateColor(color: Int) {
        this.color = color
        invalidate()
    }
}