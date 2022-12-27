package com.example.fourthtaskaston

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.ibm.icu.util.Calendar
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class RoundWallClock @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val paintCircle = Paint()
    private val paintPointer = Paint()
    private var radius = 0.0f
    private var clockColor: Int = 0
    private var circleColor: Int = 0
    private var hourColor: Int = 0
    private var minuteColor: Int = 0
    private var secondColor: Int = 0
    private var hourThickness: Float = 0.0f
    private var minuteThickness: Float = 0.0f
    private var secondThickness: Float = 0.0f
    private var circleThickness: Float = 0.0f

    init {
        initAttrs(attrs)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (min(width, height) / 2.0 * 0.8).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        postInvalidateDelayed(20)
        paintCircle.style = Paint.Style.FILL
        paintCircle.color = clockColor
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paintCircle)

        paintCircle.style = Paint.Style.STROKE
        paintCircle.color = circleColor
        paintCircle.strokeWidth = circleThickness
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paintCircle)

        for (i in 1..12) {
            canvas.drawLine(
                (width / 2).toFloat(),
                (height / 2).toFloat() - radius + 70f,
                (width / 2).toFloat(),
                (height / 2) - radius,
                paintCircle
            )
            canvas.rotate((360 / 12).toFloat(), (width / 2).toFloat(), (height / 2).toFloat())
        }

        for (i in 1..60) {
            canvas.drawLine(
                (width / 2).toFloat(),
                (height / 2).toFloat() - radius + 30f,
                (width / 2).toFloat(),
                (height / 2).toFloat() - radius + 20f,
                paintCircle
            )
            canvas.rotate((360 / 60).toFloat(), (width / 2).toFloat(), (height / 2).toFloat())
        }

        paintCircle.style = Paint.Style.FILL
        paintCircle.color = hourColor
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(),15f,paintCircle)

        val currentTime = Calendar.getInstance()
        var hours = currentTime.get(Calendar.HOUR_OF_DAY).toFloat()
        var minutes = currentTime.get(Calendar.MINUTE).toFloat()
        var seconds = currentTime.get(Calendar.SECOND).toFloat()
        val milliseconds = currentTime.get(Calendar.MILLISECOND).toFloat()

        seconds += milliseconds * 0.001f
        minutes += seconds / 60.0f
        hours += minutes / 60.0f

        paintPointer.strokeWidth = hourThickness
        paintPointer.color = hourColor
        val hourRotation = (360 / 12).toFloat() * (30 + hours)
        canvas.rotate(hourRotation, (width / 2).toFloat(), (height / 2).toFloat())
        canvas.drawLine((width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2).toFloat(),
            (height / 2).toFloat() + 250f,
            paintPointer)

        paintPointer.strokeWidth = minuteThickness
        paintPointer.color = minuteColor
        val minutesRotation = (360 / 60).toFloat() * (30 + minutes) - hourRotation
        canvas.rotate(minutesRotation, (width / 2).toFloat(), (height / 2).toFloat())
        canvas.drawLine((width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2).toFloat(),
            (height / 2).toFloat() + 300f,
            paintPointer)

        paintPointer.strokeWidth = secondThickness
        paintPointer.color = secondColor
        canvas.rotate((360 / 60).toFloat() * (30 + seconds) - minutesRotation - hourRotation,
            (width / 2).toFloat(),
            (height / 2).toFloat())
        canvas.drawLine((width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2).toFloat(),
            (height / 2).toFloat() + 300f,
            paintPointer)
    }

    private fun initAttrs(attrs: AttributeSet?){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundWallClock)
        clockColor = typedArray.getColor(R.styleable.RoundWallClock_clockColor, Color.DKGRAY)
        circleColor = typedArray.getColor(R.styleable.RoundWallClock_circleColor, Color.RED)
        hourColor = typedArray.getColor(R.styleable.RoundWallClock_hourColor, Color.BLACK)
        minuteColor= typedArray.getColor(R.styleable.RoundWallClock_minuteColor, Color.BLUE)
        secondColor = typedArray.getColor(R.styleable.RoundWallClock_secondColor, Color.GREEN)
        hourThickness = typedArray.getDimension(R.styleable.RoundWallClock_hourThickness, 20f)
        minuteThickness = typedArray.getDimension(R.styleable.RoundWallClock_minuteThickness, 15f)
        circleThickness = typedArray.getDimension(R.styleable.RoundWallClock_circleThickness, 30f)
        secondThickness = typedArray.getDimension(R.styleable.RoundWallClock_secondThickness, 10f)

        typedArray.recycle()
    }
}