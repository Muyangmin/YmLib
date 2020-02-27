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
