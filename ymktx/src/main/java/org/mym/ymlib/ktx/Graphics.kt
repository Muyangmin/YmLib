package org.mym.ymlib.ktx

import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF

fun RectF.set(left: Int, top: Int, right: Int, bottom: Int) {
    set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
}

fun Rect.set(left: Float, top: Float, right: Float, bottom: Float) {
    set(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
}

fun Rect.set(rectF: RectF) {
    set(rectF.left, rectF.top, rectF.right, rectF.bottom)
}

/**
 * 将一个颜色值转为 #AARRGGBB 或 #RRGGBB 格式的字符串。
 *
 * @param[retainAlpha] 指示返回的字符串中是否应当包含 alpha 通道值，默认为 `true`。
 */
fun Int.toColorString(retainAlpha: Boolean = true): String {
    val alpha = Color.alpha(this)
    val red = Color.red(this)
    val green = Color.green(this)
    val blue = Color.blue(this)

    return buildString {
        append("#")
        if (retainAlpha) {
            append(alpha.toStringFixedWidth(16, 2))
        }
        append(red.toStringFixedWidth(16, 2))
        append(green.toStringFixedWidth(16, 2))
        append(blue.toStringFixedWidth(16, 2))
    }
}

/**
 * 将一个整数转换为特定基数的字符串，并且使用指定的字符将结果填充为等长字符串。
 *
 * 例如：
 * ```Kotlin
 * val x = 15
 * x.toString(16) // "F"
 * x.toStringFixedWidth(16, 2)  // "0F"
 * ```
 *
 * @return 返回一个字符串，它的长度保证为 [requiredLength]。
 * @throws IllegalArgumentException 如果原始数字转换为字符串之后大于要求的长度。
 */
fun Int.toStringFixedWidth(radix: Int, requiredLength: Int, prefixWith: Char = '0'): String {
    val origin = toString(radix)
    require(origin.length <= requiredLength) {
        "Required length(${requiredLength}) must not be greater than origin length(${origin.length})"
    }

    val needToFill = requiredLength - origin.length
    return if (needToFill == 0) {
        origin
    } else {
        buildString {
            repeat(needToFill) { append(prefixWith) }
            append(origin)
        }
    }
}