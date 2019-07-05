package org.mym.ymlib.text

import android.content.Context
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat

/**
 * 专门用于设置字体的 Span。
 *
 * 项目中的常用字体推荐使用扩展字段的方式添加到 `Companion` 对象中，便于访问，例如：
 *
 * ```Kotlin
 * //NOTE: This is wrong because Extension property cannot be initialized because it has no backing field.
 * //val FontSpan.Companion.myFont = FontSpan.fromFontResource(application, R.font.myFont)
 *
 * val FontSpan.Companion.myFont
 *     get() = fromFontResource(SampleApplication.instance, R.font.myFont)
 *
 * fun sample() {
 *      textView.text = buildSpannedString {
 *          append("abc")
 *          setSpan(FontSpan.myFont, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
 *      }
 * }
 * ```
 */
class FontSpan {
    companion object {
        fun fromFontResource(context: Context, @FontRes font: Int): StyleSpan {
            return StyleSpan(ResourcesCompat.getFont(context, font)?.style ?: 0)
        }

        fun fromTypefaceName(fontFamily: String): TypefaceSpan {
            return TypefaceSpan(fontFamily)
        }
    }
}