package com.example.kidslearning.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random



class MyCanvasView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var guideLetter: String? = null
    private val letterPath = Path()
    private val letterBounds = RectF()
    private var letterPaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 12f
        isAntiAlias = true
    }

    private lateinit var maskBitmap: Bitmap
    private lateinit var maskCanvas: Canvas
    private val drawPath = Path()
    private val drawPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 40f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        maskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        maskCanvas = Canvas(maskBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        guideLetter?.let { letter ->

            // Reset
            letterPath.reset()

            // إعداد الخط
            letterPaint.textSize = height * 0.4f

            letterPaint.typeface = Typeface.create("sans-serif", Typeface.BOLD)

            // حساب العرض والارتفاع
            val textWidth = letterPaint.measureText(letter)
            val fontMetrics = letterPaint.fontMetrics
            val textHeight = fontMetrics.descent - fontMetrics.ascent

            // تحديد الإحداثيات باش يجي وسط الCanvas
            val x = (width - textWidth) / 2f
            val y = (height / 2f) - (fontMetrics.ascent + textHeight / 2f)

            // رسم Path وسط الCanvas
            letterPaint.getTextPath(letter, 0, letter.length, x, y, letterPath)

            // bounds للحرف
            letterPath.computeBounds(letterBounds, true)

            // رسم الحرف باللون الفاتح
            canvas.drawPath(letterPath, letterPaint)

            // clip داخل الحرف
            canvas.save()
            canvas.clipPath(letterPath)

            // Gradient
            val shader = LinearGradient(
                letterBounds.left, letterBounds.top,
                letterBounds.right, letterBounds.bottom,
                intArrayOf(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE),
                null,
                Shader.TileMode.MIRROR
            )

            val paint = Paint().apply {
                this.shader = shader
                style = Paint.Style.FILL
                isAntiAlias = true
                setShadowLayer(8f, 4f, 4f, Color.DKGRAY)
            }

            if (::maskCanvas.isInitialized) {
                canvas.drawBitmap(maskBitmap, 0f, 0f, paint)
            }

            canvas.restore()
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> drawPath.moveTo(event.x, event.y)
            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(event.x, event.y)
                if (::maskCanvas.isInitialized) {
                    // إعداد gradient تدريجي بدل اللون العشوائي
                    val gradient = LinearGradient(
                        0f, 0f, width.toFloat(), height.toFloat(),
                        intArrayOf(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE),
                        null,
                        Shader.TileMode.MIRROR
                    )
                    drawPaint.shader = gradient
                    maskCanvas.drawPath(drawPath, drawPaint)
                }
                drawPath.reset()
                drawPath.moveTo(event.x, event.y)
                invalidate()
            }
        }
        return true
    }


    fun drawLetterGuide(letter: String) {
        guideLetter = letter
        drawPath.reset()
        // تهيئة maskCanvas إذا مازال ما تهيأش
        if (!::maskCanvas.isInitialized && width > 0 && height > 0) {
            maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            maskCanvas = Canvas(maskBitmap)
        }
        if (::maskCanvas.isInitialized) {
            maskCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        }
        invalidate()
    }

    fun clearCanvas() {
        drawLetterGuide(guideLetter ?: "")
    }
}
