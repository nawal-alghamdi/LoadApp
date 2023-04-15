package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.content.withStyledAttributes
import kotlin.math.min
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0

    private var buttonColor: Int = 0
    private var loadingColor: Int = 0
    private var circleColor: Int = 0

    private var progressWidth = 0f
    private var sweepAngle = 0f

    private val textBound = Rect()

    private var label = resources.getString(R.string.download)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var valueAnimator = ValueAnimator()

    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) {
            kProperty: KProperty<*>, buttonStateOld: ButtonState, buttonStateNew: ButtonState ->
        when (buttonStateNew) {
            ButtonState.Clicked -> {
                label = resources.getString(R.string.button_loading)
                invalidate()
                animateButton()
            }
            ButtonState.Completed -> {
                valueAnimator.repeatCount = 0
                invalidate()
            }
        }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonColor = getColor(R.styleable.LoadingButton_buttonColor, 0)
            loadingColor = getColor(R.styleable.LoadingButton_loadingColor, 0)
            circleColor = getColor(R.styleable.LoadingButton_circleColor, 0)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = buttonColor
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
        //Button loading
        paint.color = loadingColor
        canvas.drawRect(0f, 0f, progressWidth, heightSize.toFloat(), paint)

        // Draw the text label
        paint.color = Color.WHITE
        paint.getTextBounds(label, 0, label.length, textBound)
        canvas.drawText(
            label,
            (widthSize / 2).toFloat(),
            heightSize / 2 - textBound.exactCenterY(),
            paint
        )
        //Animate circle
        paint.color = circleColor
        val radius = (min(widthSize, heightSize) / 2.0 * 0.4).toFloat()
        val arcLeft = (widthSize / 2) + textBound.exactCenterX()
        val arcTop = (heightSize / 2) - radius
        canvas.drawArc(
            arcLeft, arcTop, arcLeft + radius * 2, arcTop + radius * 2,
            0f, sweepAngle, true, paint
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

    private fun animateButton() {
        valueAnimator = ValueAnimator.ofFloat(0f, widthSize.toFloat())
        valueAnimator.duration = 1500
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.addUpdateListener {
            progressWidth = it.animatedValue as Float
            sweepAngle = (it.animatedValue as Float / 2.5).toInt().toFloat()
            invalidate()
        }
        valueAnimator.start()

        valueAnimator.doOnEnd {
            progressWidth = 0f
            sweepAngle = 0f
            label = resources.getString(R.string.download)
            invalidate()
        }
    }

}