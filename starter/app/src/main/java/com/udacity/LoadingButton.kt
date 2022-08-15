package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonColor = 0
    private val textBound = Rect()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }


    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonColor = getColor(R.styleable.LoadingButton_buttonColor, 0)
        }

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = buttonColor
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        // Draw the text label
        paint.color = Color.WHITE
        val label = resources.getString(R.string.download)
        paint.getTextBounds(label, 0, label.length, textBound)
        canvas.drawText(
            label,
            (widthSize / 2).toFloat(),
            heightSize / 2 - textBound.exactCenterY(),
            paint
        )

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}