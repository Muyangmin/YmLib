package org.mym.ymlib.ktx

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