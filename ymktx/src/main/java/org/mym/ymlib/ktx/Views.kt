package org.mym.ymlib.ktx

import android.view.View

/**
 * 将参数 view 的可见性全部设置为 [View.VISIBLE]。
 *
 * @since 0.7.0
 */
fun makeViewsVisible(vararg view: View) = view.setAllVisibility(View.VISIBLE)

/**
 * 将参数 view 的可见性全部设置为 [View.INVISIBLE]。
 *
 * @since 0.7.0
 */
fun makeViewsInvisible(vararg view: View) = view.setAllVisibility(View.INVISIBLE)

/**
 * 将参数 view 的可见性全部设置为 [View.GONE]。
 *
 * @since 0.7.0
 */
fun makeViewsGone(vararg view: View) = view.setAllVisibility(View.GONE)

/**
 * 批量修改 View 的可见性。
 *
 * @since 0.7.0
 */
fun Collection<View>.setAllVisibility(visibility: Int) = forEach {
    it.visibility = visibility
}

/**
 * 批量修改 View 的可见性。
 *
 * @since 0.7.0
 */
fun Array<out View>.setAllVisibility(visibility: Int) = forEach {
    it.visibility = visibility
}

/**
 * 根据 [predicate] 来判断应当为此视图设置何种可见性。
 *
 * @param[predicate] 判断条件。该函数的执行不应该产生 side effect.
 * @param[otherwise] 条件不成立时应设置为何种可见性，默认为 [View.GONE]。
 */
inline fun View.visibleIf(otherwise: Int = View.GONE, predicate: () -> Boolean) {
    visibility = if (predicate.invoke()) {
        View.VISIBLE
    } else {
        otherwise
    }
}