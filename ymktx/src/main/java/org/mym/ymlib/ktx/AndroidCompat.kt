/**
 * 这个文件主要用于将各种 XXXCompat 相关类修改为对应的扩展方法，使得其使用起来更为自然。
 */
package org.mym.ymlib.ktx

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

/**
 * 等价于调用 `ContextCompat.getColor(this, colorRes)`。
 */
@ColorInt
fun Context.getColorCompat(@ColorRes colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

/**
 * 等价于调用 `ResourcesCompat.getFont(this, id)`。
 */
fun Context.getFontCompat(@FontRes id: Int): Typeface? = ResourcesCompat.getFont(this, id)

/**
 * 等价于调用 `setTextColor`，但会使用 [getColorCompat]。
 */
fun TextView.setTextColorCompat(@ColorRes colorRes: Int) {
    setTextColor(context.getColorCompat(colorRes))
}