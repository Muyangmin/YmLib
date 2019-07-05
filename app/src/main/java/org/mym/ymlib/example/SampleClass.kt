package org.mym.ymlib.example

import android.text.Spannable
import androidx.core.text.buildSpannedString
import org.mym.ymlib.text.FontSpan

//SampleCalls only
@Suppress("unused")
class SampleClass {

    private val FontSpan.Companion.dinotBold
        get() = fromFontResource(SampleApplication.instance, R.font.dinot_bold)

    fun sampleFontSpan() {
        buildSpannedString {
            append("abc")
            setSpan(FontSpan.dinotBold, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}