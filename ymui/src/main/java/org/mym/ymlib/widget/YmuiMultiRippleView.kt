package org.mym.ymlib.widget

import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import org.mym.ymlib.ktx.toColorString
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * 用于绘制多层同心圆扩散式水波纹效果的View。
 *
 * 支持自定义的属性：
 * * [R.styleable.YmuiMultiRippleView_ymui_paintColor] 画笔基础颜色，alpha 值会被忽略
 * * [R.styleable.YmuiMultiRippleView_ymui_circleMode] 填充模式
 * * [R.styleable.YmuiMultiRippleView_ymui_maxCircles] 最大允许存在的圆环数目
 * * [R.styleable.YmuiMultiRippleView_ymui_duration] 周期长度，单位为毫秒
 * * [R.styleable.YmuiMultiRippleView_ymui_ringWidth] 圆环宽度
 * * [R.styleable.YmuiMultiRippleView_ymui_startRadius] 最小圆半径
 * * [R.styleable.YmuiMultiRippleView_ymui_startAlpha] 最小圆透明度
 * * [R.styleable.YmuiMultiRippleView_ymui_endRadius] 最大圆半径
 * * [R.styleable.YmuiMultiRippleView_ymui_endAlpha] 最大圆透明度
 */
class YmuiMultiRippleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val CIRCLE_MODE_FILL = 0
        const val CIRCLE_MODE_RING = 1
    }

    @IntDef(CIRCLE_MODE_FILL, CIRCLE_MODE_RING)
    @Retention(AnnotationRetention.SOURCE)
    annotation class CircleMode

    @ColorInt
    private var paintColor: Int

    @CircleMode
    private var circleMode: Int

    private var maxCircles: Int

    private var duration: Int

    private var ringWidth: Int

    private var startRadius: Int

    private var endRadius: Int

    private var startAlpha: Float

    private var endAlpha: Float

    private var fractionPerCircle: Float

    private val paint: Paint

    /**
     * 圆心位置。布局时自动获取视图中心点，不可指定。
     */
    private val center: PointF = PointF()

    /**
     * 动画计算器，借用 alpha 通道值来做其参数。
     */
    private var animator: ValueAnimator

    private var floatEvaluator = FloatEvaluator()

    private var needDrawCircles: Int = 0

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.YmuiMultiRippleView)
        paintColor = a.getColor(R.styleable.YmuiMultiRippleView_ymui_paintColor, Color.WHITE)
        circleMode = a.getInteger(R.styleable.YmuiMultiRippleView_ymui_circleMode, CIRCLE_MODE_FILL)
        maxCircles = a.getInteger(R.styleable.YmuiMultiRippleView_ymui_maxCircles, 4)
        duration = a.getInteger(R.styleable.YmuiMultiRippleView_ymui_duration, 2000)
        ringWidth = a.getDimensionPixelSize(R.styleable.YmuiMultiRippleView_ymui_ringWidth, 15)
        startRadius = a.getDimensionPixelSize(R.styleable.YmuiMultiRippleView_ymui_startRadius, 0)
        endRadius =
            a.getDimensionPixelSize(R.styleable.YmuiMultiRippleView_ymui_endRadius, Int.MAX_VALUE)
        startAlpha = a.getFloat(R.styleable.YmuiMultiRippleView_ymui_startAlpha, 1F)
        endAlpha = a.getFloat(R.styleable.YmuiMultiRippleView_ymui_endAlpha, 0F)
        a.recycle()

        fractionPerCircle = 1F / maxCircles
        Log.v(
            VIEW_LOG_TAG,
            "init param: paintColor=${paintColor.toColorString()}, fractionPerCircle = $fractionPerCircle"
        )

        animator = ValueAnimator.ofFloat(1F, 0F).apply {
            duration = this@YmuiMultiRippleView.duration.toLong()
            addUpdateListener {
                postInvalidate()
            }
            repeatCount = ValueAnimator.INFINITE
        }

        paint = Paint()
        paint.style = if (circleMode == CIRCLE_MODE_FILL) {
            Paint.Style.FILL
        } else {
            Paint.Style.STROKE
        }
        paint.isAntiAlias = true
        paint.strokeWidth = ringWidth.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val requiredSize = endRadius.takeIf { endRadius == Int.MAX_VALUE } ?: endRadius * 2
        val width = getSize(widthMeasureSpec, requiredSize)
        val height = getSize(heightMeasureSpec, requiredSize)
//        Log.v(VIEW_LOG_TAG, "measured width=$width, height=$height")
        setMeasuredDimension(width, height)
    }

    private fun getSize(parentSpec: Int, requireSize: Int): Int {
        val specMode = MeasureSpec.getMode(parentSpec)
        val specSize = MeasureSpec.getSize(parentSpec)

        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> min(specSize, requireSize)
            else /*MeasureSpec.UNSPECIFIED*/ -> requireSize
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            center.x = width / 2F
            center.y = height / 2F
//            Log.v(VIEW_LOG_TAG, "left=$left, right=$right, centerX = ${center.x}")
//            Log.v(VIEW_LOG_TAG, "top=$top, bottom=$bottom, centerY = ${center.y}")
            //计算对角线长度，作为圆圈的实际最大半径
            val width = right - left
            val height = bottom - top
            val diagonal = sqrt((width * width + height * height).toDouble()) / 2.0
            endRadius = min(endRadius, diagonal.roundToInt())
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val currentFraction = animator.animatedFraction
        //例如每个圆圈占比 0.25, 当前位置0.76，则需要画四个圈
        if (needDrawCircles < maxCircles) {
            needDrawCircles = (currentFraction / fractionPerCircle).toInt() + 1
        }
//        Log.v(VIEW_LOG_TAG, "currentFraction = $currentFraction, circlesToDraw: $needDrawCircles")
        for (index in 0 until needDrawCircles) {
            val fraction = index * fractionPerCircle + currentFraction.rem(fractionPerCircle)
            val radius =
                floatEvaluator.evaluate(fraction, startRadius.toFloat(), endRadius.toFloat())
            val alpha =
                (floatEvaluator.evaluate(fraction, startAlpha * 255F, endAlpha * 255F)).roundToInt()
            val color = Color.argb(alpha, paintColor.red, paintColor.green, paintColor.blue)
            paint.color = color
//            Log.v(
//                VIEW_LOG_TAG,
//                "circle $index: fraction=$fraction, radius=$radius, color=${color.toColorString()}"
//            )
            canvas.drawCircle(center.x, center.y, radius, paint)
        }
    }

    fun startAnimation() {
        animator.start()
    }

    fun pauseAnimation() {
        animator.pause()
    }

    fun resumeAnimation() {
        animator.resume()
    }

    fun stopAnimation() {
        animator.cancel()
        needDrawCircles = 0
    }
}