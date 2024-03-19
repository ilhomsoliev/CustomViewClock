package com.ilhomsoliev.customviewclock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.sin


class AnalogClockView(
    context: Context,
    attributeSet: AttributeSet,
) : View(context, attributeSet) {
    private var height = 0
    private var width = 0
    private var radius = 0
    private var angle = 0.0
    private var centreX = 0
    private var centreY = 0
    private var padding = 0
    private var isInit = false
    private lateinit var paint: Paint
    private lateinit var rect: Rect
    private var hour = 0f
    private var minute = 0f
    private var second = 0f
    private var hourHandSize = 0
    private var handSize = 0

    private fun init() {
        height = getHeight()
        width = getWidth()
        padding = 50
        centreX = width / 2
        centreY = height / 2
        val minimum = height.coerceAtMost(width)
        radius = (minimum / 2 - padding).plus(40)
        angle = (Math.PI / 30 - Math.PI / 2).toFloat().toDouble()
        paint = Paint()
        rect = Rect()
        hourHandSize = radius - radius / 2
        handSize = radius - radius / 4
        isInit = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isInit) init()
        drawCircle(canvas)
        drawHands(canvas)
        drawNumerals(canvas)
        drawCircleCenter(canvas)
        postInvalidateDelayed(1000)
    }

    private fun drawCircleCenter(canvas: Canvas) {
        paint.reset()
        setPaintAttributes(Color.BLACK, Paint.Style.FILL, 8);
        canvas.drawCircle(centreX.toFloat(), centreY.toFloat(), 20.toFloat(), paint)
    }

    private fun drawHands(canvas: Canvas) {
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR_OF_DAY).toFloat()
        hour = if (hour > 12) hour - 12 else hour
        minute = calendar.get(Calendar.MINUTE).toFloat()
        second = calendar.get(Calendar.SECOND).toFloat()
        drawHourArrow(canvas, (hour + minute / 60.0) * 5f)
        drawMinuteArrow(canvas, minute)
        drawSecondsArrow(canvas, second)
    }

    private fun drawCircle(canvas: Canvas) {
        paint.reset();
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 8);
        canvas.drawCircle(centreX.toFloat(), centreY.toFloat(), radius.toFloat(), paint);
    }

    private fun setPaintAttributes(colour: Int, stroke: Paint.Style, strokeWidth: Int) {
        paint.reset()
        paint.color = colour
        paint.style = stroke
        paint.strokeWidth = strokeWidth.toFloat()
        paint.isAntiAlias = true
    }

    private fun drawMinuteArrow(canvas: Canvas, location: Float) {
        paint.reset()
        setPaintAttributes(Color.BLUE, Paint.Style.STROKE, 8);
        angle = Math.PI * location / 30 - Math.PI / 2;
        canvas.drawLine(
            centreX.toFloat(),
            centreY.toFloat(),
            (centreX + cos(angle) * handSize).toFloat(),
            (centreY + sin(angle) * hourHandSize).toFloat(),
            paint
        )
    }

    private fun drawHourArrow(canvas: Canvas, location: Double) {
        paint.reset()
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 16)
        angle = Math.PI * location / 30 - Math.PI / 2
        canvas.drawLine(
            centreX.toFloat(),
            centreY.toFloat(),
            (centreX + cos(angle) * hourHandSize).toFloat(),
            (centreY + sin(angle) * hourHandSize).toFloat(),
            paint
        )
    }

    private fun drawSecondsArrow(canvas: Canvas, location: Float) {
        paint.reset()
        setPaintAttributes(Color.RED, Paint.Style.STROKE, 8)
        angle = Math.PI * location / 30 - Math.PI / 2
        canvas.drawLine(
            centreX.toFloat(),
            centreY.toFloat(),
            (centreX + cos(angle) * handSize).toFloat(),
            (centreY + sin(angle) * hourHandSize).toFloat(),
            paint
        )
    }

    private fun drawNumerals(canvas: Canvas) {
        paint.reset();
        setPaintAttributes(Color.BLACK, Paint.Style.FILL, 10)
        paint.textSize = spToPx(40f, context)
        for (number in intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)) {
            val num = number.toString()
            paint.getTextBounds(num, 0, num.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val numRadius = radius.minus(80)
            val x = (centreX + cos(angle) * numRadius - rect.width() / 2).toFloat()
            val y = (centreY + sin(angle) * numRadius + rect.height() / 2).toFloat()
            canvas.drawText(num, x, y, paint)
        }
    }

}

fun spToPx(sp: Float, context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        context.resources.displayMetrics
    )
}