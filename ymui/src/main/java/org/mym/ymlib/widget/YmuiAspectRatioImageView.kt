package org.mym.ymlib.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.getFloatOrThrow
import kotlin.math.roundToInt

/**
 * 可以指定宽高比的 ImageView。
 *
 * ### 使用方法
 * 直接指定 `ymui_ratio` 即可。
 * ```XML
 *     <org.mym.ymlib.widget.YmuiAspectRatioImageView
 *          android:layout_width="match_parent"
 *          android:layout_height="wrap_content"
 *          app:ymui_ratio="1.5"/>
 * ```
 */
class YmuiAspectRatioImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val ratio: Float

    init {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.YmuiAspectRatioImageView)
        ratio = arr.getFloatOrThrow(R.styleable.YmuiAspectRatioImageView_ymui_ratio)
        arr.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = (MeasureSpec.getSize(widthMeasureSpec) / ratio).roundToInt()
        val heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightSpec)
    }
}