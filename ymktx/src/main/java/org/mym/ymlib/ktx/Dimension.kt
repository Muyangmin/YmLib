package org.mym.ymlib.ktx

import android.content.res.Resources
import android.util.TypedValue

typealias PxDimension = Float

/**
 * 将当前值视为 [TypedValue.COMPLEX_UNIT_DIP] 时对应的 pixel 值。
 */
val Int.dp: PxDimension
    get() = applyDimensionInternal(TypedValue.COMPLEX_UNIT_DIP, this.toFloat())

/**
 * 将当前值视为 [TypedValue.COMPLEX_UNIT_DIP] 时对应的 pixel 值。
 */
val Float.dp: PxDimension
    get() = applyDimensionInternal(TypedValue.COMPLEX_UNIT_DIP, this)

/**
 * 将当前值视为 [TypedValue.COMPLEX_UNIT_SP] 时对应的 pixel 值。
 */
val Int.sp: PxDimension
    get() = applyDimensionInternal(TypedValue.COMPLEX_UNIT_SP, this.toFloat())

/**
 * 将当前值视为 [TypedValue.COMPLEX_UNIT_SP] 时对应的 pixel 值。
 */
val Float.sp: PxDimension
    get() = applyDimensionInternal(TypedValue.COMPLEX_UNIT_SP, this)

private fun applyDimensionInternal(unit: Int, value: Float): Float {
    val displayMetrics = Resources.getSystem().displayMetrics
    return TypedValue.applyDimension(unit, value, displayMetrics)
}