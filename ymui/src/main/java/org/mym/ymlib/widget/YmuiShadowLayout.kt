package org.mym.ymlib.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.Px
import kotlin.math.max

/**
 * 这个布局用于绘制阴影效果。
 *
 * * 支持矩形和（椭）圆形阴影
 * * 支持单独控制矩形每一条边是否绘制阴影
 * * 支持代码中动态修改阴影效果
 * * 支持 wrap_content 布局，兼容 RecyclerView 使用
 * * 支持硬件加速 （API 28+）
 *
 * *注意： 该布局继承自 [FrameLayout]，但不做向前兼容的保证；建议只放置一个子布局，并且不要依赖于这个继承关系。*
 */
class YmuiShadowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        // 阴影形状
        private const val SHAPE_RECTANGLE = 1
        private const val SHAPE_OVAL = 2

        //矩形四条边的阴影单独控制位，仅矩形有效
        const val SIDE_LEFT = 1 shl 0
        const val SIDE_TOP = 1 shl 1
        const val SIDE_RIGHT = 1 shl 2
        const val SIDE_BOTTOM = 1 shl 3
        const val SIDE_ALL = SIDE_LEFT or SIDE_TOP or SIDE_RIGHT or SIDE_BOTTOM
    }

    @ColorInt
    private var shadowColor: Int

    @Px
    private var shadowRadius: Int

    private var shadowSide: Int

    private var shadowShape: Int

    @Px
    private var shadowDx: Int

    @Px
    private var shadowDy: Int

    private val shadowPaint: Paint = Paint()

    /**
     * 用于保存阴影宽度矩阵，避免二次测量。注意这个矩阵并非直接用于绘制。
     */
    private val paddingRect: Rect = Rect()

    /**
     * 实际用于绘制的矩阵。
     */
    private val drawRectF: RectF = RectF()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.YmuiShadowLayout)
        shadowColor = a.getColor(R.styleable.YmuiShadowLayout_ymui_shadowColor, Color.BLACK)
        shadowRadius = a.getDimensionPixelSize(R.styleable.YmuiShadowLayout_ymui_shadowRadius, 0)
        shadowShape = a.getInteger(R.styleable.YmuiShadowLayout_ymui_shadowShape, SHAPE_RECTANGLE)
        shadowSide = a.getInteger(R.styleable.YmuiShadowLayout_ymui_shadowSide, SIDE_ALL)
        shadowDx = a.getDimensionPixelSize(R.styleable.YmuiShadowLayout_ymui_shadowDx, 0)
        shadowDy = a.getDimensionPixelSize(R.styleable.YmuiShadowLayout_ymui_shadowDy, 0)
        a.recycle()

        resetShadowPaint()

        // 提示需要执行 onDraw() 方法
        setWillNotDraw(false)
        // API 28- 硬件加速不支持阴影
        // See https://developer.android.com/guide/topics/graphics/hardware-accel#unsupported
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        computePaddingRect()
        setPadding(
            paddingRect.left,
            paddingRect.top,
            paddingRect.right,
            paddingRect.bottom
        )
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        computeDrawingRectF()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isInEditMode) {
            return
        }
        when (shadowShape) {
            SHAPE_RECTANGLE -> {
                canvas.drawRect(drawRectF, shadowPaint)
            }
            SHAPE_OVAL -> {
                canvas.drawOval(drawRectF, shadowPaint)
            }
        }
    }

    private fun resetShadowPaint() {
        shadowPaint.apply {
            reset()
            isAntiAlias = true
            color = Color.TRANSPARENT
            setShadowLayer(shadowRadius.toFloat(), shadowDx.toFloat(), shadowDy.toFloat(), shadowColor)
        }
    }

    private fun computePaddingRect() {
        val left = max(
            0, if (shouldShowShadowForSide(SIDE_LEFT)) {
                shadowRadius
            } else {
                0
            } - shadowDx
        )
        val top = max(
            0, if (shouldShowShadowForSide(SIDE_TOP)) {
                shadowRadius
            } else {
                0
            } - shadowDy
        )
        val right = if (shouldShowShadowForSide(SIDE_RIGHT)) {
            shadowRadius
        } else {
            0
        } + shadowDx
        val bottom = if (shouldShowShadowForSide(SIDE_BOTTOM)) {
            shadowRadius
        } else {
            0
        } + shadowDy
        paddingRect.set(left, top, right, bottom)
    }

    private fun computeDrawingRectF() {
        drawRectF.set(
            paddingRect.left.toFloat(),
            paddingRect.top.toFloat(),
            (measuredWidth - paddingRect.right).toFloat(),
            (measuredHeight - paddingRect.bottom).toFloat()
        )
    }

    private fun shouldShowShadowForSide(sideFlag: Int): Boolean {
        return shadowSide and sideFlag == sideFlag
    }

    /**
     * 动态设置阴影颜色。
     */
    fun setShadowColor(@ColorInt color: Int) {
        shadowColor = color
        resetShadowPaint()
        invalidate()
    }

    /**
     * 动态设置阴影半径。
     */
    fun setShadowRadius(@Px radius: Int) {
        shadowRadius = radius
        resetShadowPaint()
        requestLayout()
    }

    fun setShadowDx(@Px dx: Int) {
        shadowDx = dx
        resetShadowPaint()
        requestLayout()
    }

    fun setShadowDy(@Px dy: Int) {
        shadowDy = dy
        resetShadowPaint()
        requestLayout()
    }

    fun setShadowSide(side: Int) {
        shadowSide = side
        requestLayout()
    }
}